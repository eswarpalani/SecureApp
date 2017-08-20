package com.virtusa.virtusabank.secureapp.controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;

import com.virtusa.virtusabank.secureapp.constants.Constants;
import com.virtusa.virtusabank.secureapp.dao.CustomerRepository;
import com.virtusa.virtusabank.secureapp.dao.TransactionRepository;
import com.virtusa.virtusabank.secureapp.model.Customer;
import com.virtusa.virtusabank.secureapp.model.StatementInfo;
import com.virtusa.virtusabank.secureapp.model.Transaction;
import com.virtusa.virtusabank.secureapp.model.Transaction_;

/**
 * Spring Controller Definitions.
 */
@Controller
public class MyController {

	@Autowired
	DataSource dataSoruce;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	TransactionRepository transactionRepository;

	@Autowired
	ServletContext servletContext;

	@RequestMapping("/")
	public String init(Map<String, Object> model, Principal principal,
			HttpServletRequest req, HttpServletResponse resp) {
		String username = getUserName(principal);
		Collection<String> roles = getUserRoles(principal);
		model.put("username", username);
		model.put("userroles", roles);
		String[] menus;
		if (roles.contains("ROLE_ADMIN")) {
			menus = new String[] { "Customer List", "Onboard Customer", "Account Statement",
					"Fund Transfer" };
			model.put("menus", Constants.ADMIN_MENU);
		} else {
			menus = new String[] { "Account Statement", "Fund Transfer" };
			model.put("menus", Constants.USER_MENU);
		}
		WebContext ctx = new WebContext(req, resp, servletContext, req.getLocale());
		ctx.setVariable("username", username);
		ctx.setVariable("actions", menus);
		ctx.setVariable("user_id", getUserId(username));
		if (username.equals("anonymous")) {
			return "login";
		} else {
			return "app";
		}

	}

	@RequestMapping(value = "/saveCustomer", method = RequestMethod.POST)
	public @ResponseBody Customer saveCustomer(@RequestBody Customer customer) {
		customer.setBalance(10000.0);
		customer.setSsn(Base64.getEncoder().withoutPadding().encodeToString(customer.getSsn() .getBytes()));
		customerRepository.save(customer);
		return customer;
	}
	
	@RequestMapping(value = "/getCustomerList", method = RequestMethod.GET)
	public @ResponseBody List<Customer> getCustomerList(HttpServletRequest req,
			HttpServletResponse resp) {

		return customerRepository.findAll();
	}

	@RequestMapping(value = "/getFundTransferAccount", method = RequestMethod.GET)
	public @ResponseBody Customer transferFund(HttpServletRequest req,
			HttpServletResponse resp) {

		Customer customer = customerRepository.findOne(1);
		return customer;
	}

	@RequestMapping(value = "/fundTransfer", method = RequestMethod.POST)
	public @ResponseBody Customer fundTransfer(
			@RequestBody Transaction transaction) {
		Customer customer = customerRepository.findOne(transaction.getFromAccSeqId());
		Customer cust2 = customerRepository.findOne(transaction.getToAccSeqId());
		double balance = customer.getBalance() - transaction.getAmoutTransferred();
		if(balance<0) {
			return null;
		}
		customer.setBalance(balance);
		if(cust2 != null) {
			cust2.setBalance(cust2.getBalance()+transaction.getAmoutTransferred());
		}
		transactionRepository.saveAndFlush(transaction);
		customerRepository.saveAndFlush(customer);
		
		return customer;
	}

	@RequestMapping(value = "/getStatement", method = RequestMethod.POST)
	public @ResponseBody List<Transaction> getTransactions(final @RequestBody StatementInfo info) {
		return transactionRepository.findAll(new Specification<Transaction>() {

			@Override
			public Predicate toPredicate(Root<Transaction> root,
					CriteriaQuery<?> cq, CriteriaBuilder cb) {
				Predicate p1 = cb.or(cb.equal(
						root.get(Transaction_.fromAccSeqId), info.getFromAccId()), cb
						.equal(root.get(Transaction_.toAccSeqId), info.getFromAccId()));
				Predicate p2 = cb.and(cb.greaterThan(
						root.get(Transaction_.timeMilliSec),
						info.getFrom()),
						cb.lessThan(root.get(Transaction_.timeMilliSec),
								info.getTo()));
				return cb.and(p1, p2);
			}
		});
	}

	private String getUserName(Principal principal) {
		if (principal == null) {
			return "anonymous";
		} else {

			final UserDetails currentUser = (UserDetails) ((Authentication) principal)
					.getPrincipal();
			Collection<? extends GrantedAuthority> authorities = currentUser
					.getAuthorities();
			for (GrantedAuthority grantedAuthority : authorities) {
				System.out.println(grantedAuthority.getAuthority());
			}
			System.out.println("principal name=======" + principal.getName());
			return principal.getName();
		}
	}

	private Collection<String> getUserRoles(Principal principal) {
		if (principal == null) {
			return Arrays.asList("none");
		} else {

			Set<String> roles = new HashSet<String>();

			final UserDetails currentUser = (UserDetails) ((Authentication) principal)
					.getPrincipal();
			Collection<? extends GrantedAuthority> authorities = currentUser
					.getAuthorities();
			for (GrantedAuthority grantedAuthority : authorities) {
				roles.add(grantedAuthority.getAuthority());
			}
			return roles;
		}
	}
	
	private String getUserId(String username) {
		return username.equals("eswar")?"1":username.equalsIgnoreCase("viswa")?"2":"3";
	}

}