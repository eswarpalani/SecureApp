(function () {
    var app = angular.module('CBHApp', ["ngRoute"]);

      app.controller('navController', function() {
        this.tab = "";

        this.isSet = function(checkTab) {
          return this.tab == checkTab;
        };

        this.setTab = function(setTab) {
          this.tab = setTab;
        };
      });

      app.controller('onBoardController', function($scope, $http) {

        this.loadData = function() {
            $http({
                method: 'GET',
                url: '/getCustomerList'

              }).then(function successCallback(response) {
                  var history = response.data;
                  var custData = document.getElementById('cust_detail');
                  custData.innerHTML = '';
                  history.forEach(function(data) {
                    var row = document.createElement('tr');
                    var fne = document.createElement('td');
                    var lne = document.createElement('td');
                    var dob = document.createElement('td');
                    var ssne = document.createElement('td');
                    var bal = document.createElement('td');

                    fne.innerHTML=data.firstName;
                    lne.innerHTML=data.lastName;
                    dob.innerHTML=data.dob;
                    try{
                    	 ssne.innerHTML= atob(data.ssn);
                    } catch(e) {
                     ssne.innerHTML= data.ssn;
                    }
                    bal.innerHTML= data.balance;
                    row.appendChild(fne);
                    row.appendChild(lne);
                    row.appendChild(dob);
                    row.appendChild(ssne);
                    row.appendChild(bal);

                    custData.appendChild(row);
                  });
              });
              return true;
            }
            this.loadData();

        this.saveCustomer = function() {
          var customer = {
            'firstName' : document.getElementById('firstname_txt').value,
            'lastName' : document.getElementById('lastname_txt').value,
            'dob' : document.getElementById('dob_text').value,
            'ssn' : document.getElementById('SSN_text').value,
          };
          $http({
            method: 'POST',
            url: '/saveCustomer',
            data: customer
          }).then(function successCallback(response) {
              $.growl.notice({ title: "Success", message: "Added Customer with id:"+response.data.id });
              this.loadData();
          }, function errorCallback(response) {
              $.growl.error({ title: "Error", message: "Error in adding customer" });
          });
        }
      });

      app.controller('statementController', function($scope, $http) {
        this.transactions = [];

        this.hasTransactions = function() {
          return this.transactions.length != 0;
        }

        this.addTransactions = function(transactions) {
          this.transactions = transactions;
        }

        this.getTransactions = function() {
          var fromDt = new Date(document.getElementById('tran_from_dt').value);
          var toDt = new Date(document.getElementById('tran_to_dt').value);
          if( (fromDt == 'Invalid Date' || toDt == 'Invalid Date') || fromDt > toDt) {
             $.growl.error({ title: "Warning!", message: "Please select valid date range" });
            return false;
          }
          var payload = {
            'fromAccId':document.getElementById('u_id').value,
            'from':fromDt.getTime(),
            'to':toDt.getTime()
          };
          $http({
            method: 'POST',
            url: '/getStatement',
            data: payload
          }).then(function successCallback(response) {
              var history = response.data;
              if(history.length == 0){
              	 $.growl.warning({ message: "No Data available" });
              }
              var transacData = document.getElementById('tr_detail');
              transacData.innerHTML = '';
              history.forEach(function(data) {
                var row = document.createElement('tr');
                var fromElement = document.createElement('td');
                var toElement = document.createElement('td');
                var dataElement = document.createElement('td');
                var dateElement = document.createElement('td');
                var comment = document.createElement('td');

                var dd = new Date(data.timeMilliSec);

                fromElement.innerHTML=data.fromAccSeqId;
                toElement.innerHTML=data.toAccSeqId;
                dataElement.innerHTML=data.amoutTransferred;
                comment.innerHTML = data.comment
                dateElement.innerHTML= (dd.getMonth()+1) + '/' +dd.getDate()+'/'+dd.getYear() +' '+ dd.getHours() +':'+  dd.getMinutes();

                row.appendChild(fromElement);
                row.appendChild(toElement);
                row.appendChild(dataElement);
                row.appendChild(dateElement);
                row.appendChild(comment);
                document.getElementById('tr_detail').appendChild(row);
              });
          }, function errorCallback(response) {
               $.growl.warning({ message: "No Data available" });
          });
        }

      });


      app.controller('fundTransferController', function($scope, $http) {
        this.fundTransferAccount = [];
        $http({
          method: 'GET',
          url:'/getFundTransferAccount',
        }).then(function successCallback(response) {
          if(response.data != '')
            this.fundTransferAccount = [response.data];
        });

        this.transfer = function() {
          var transaction = {
            'fromAccSeqId': document.getElementById('from_acc_id').value,
            'toAccSeqId': document.getElementById('to_acc_txt').value,
            'amoutTransferred': document.getElementById('amount_text').value,
            'timeMilliSec': new Date().getTime(),
            'comment': document.getElementById('description_text').value
          }
          $http({
            method: 'POST',
            url:'/fundTransfer',
            data: transaction
          }).then(function successCallback(response) {
            if(response.data != '')
               $.growl.notice({ title: "Success", message: "Transfer success, your new balance is: "+response.data.balance });
          });
        }
      });
      document.addEventListener('contextmenu', function(event){
    	  event.preventDefault();
      }, false);
})();
