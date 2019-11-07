# Jersey REST API TEST

## Build & run

```bash
mvn clean install
java -jar target/jersey-test-1.0-SNAPSHOT-shaded.jar
```

The server listens to port 9998 by default but it can be changed in `app.properties`
inside `resources`

Here are some sample curl commands to test the service:

```bash
curl -v -d '{"amount":"10", "currency":"USD"}' -H "Content-Type: application/json" -X POST 'http://localhost:9998/jersey-test/api/accounts'
curl -v -d '{"amount":"10", "currency":"EUR"}' -H "Content-Type: application/json" -X POST 'http://localhost:9998/jersey-test/api/accounts'
curl -v -X GET 'http://localhost:9998/jersey-test/api/accounts?page=0&pageSize=10'
curl -v -X GET 'http://localhost:9998/jersey-test/api/accounts/1'
curl -v -d '{"amount":"20", "version": 0}' -H "Content-Type: application/json" -X PUT 'http://localhost:9998/jersey-test/api/accounts/1'
curl -v -d '{"amount":"10", "from":{"id":1}, "to":{"id":2}}' -H "Content-Type: application/json" -X POST 'http://localhost:9998/jersey-test/api/accounts/transfers'
curl -v -X GET http://localhost:9998/jersey-test/api/accounts/1/transfers/1
curl -v -X GET 'http://localhost:9998/jersey-test/api/accounts/1/transfers?page=0&pageSize=10'
```

Also there is a minimal rest documentation created by jaxrs-analyzer which is generated 
automatically by maven.
Here is a version provided. While it is not accurate, but may come in handy:

```
= REST resources of jersey-test
1.0-SNAPSHOT

== `GET jersey-test/api/accounts`

=== Request
_No body_ + 
*Query Param*: page, int + 
*Query Param*: pageSize, int + 

=== Response
*Content-Type*: `application/json`

==== `200 OK`
*Response Body*: (`com.github.madz0.jerseytest.repository.Page<com.github.madz0.jerseytest.model.Account>`) + 
`application/json`: `{"contents":[{"currency":"string","amount":0.0}],"totalSize":0,"page":0,"pageSize":0}` + 

== `POST jersey-test/api/accounts`

=== Request
*Content-Type*: `application/json` + 
*Request Body*: (`com.github.madz0.jerseytest.model.Account`) + 
`application/json`: `{"currency":"string","amount":0.0}` + 

=== Response
*Content-Type*: `application/json`

==== `201 Created`
*Header*: `Location` + 

== `POST jersey-test/api/accounts/transfers`

=== Request
*Content-Type*: `application/json` + 
*Request Body*: (`com.github.madz0.jerseytest.model.Transfer`) + 
`application/json`: `{"amountInDestinationCurrency":0.0,"fromAccountId":0,"toAccountId":0,"originCurrency":"string","destinationCurrency":{},"roundedAmount":0.0,"exchangeDateAndTime":"date","from":{"currency":{},"amount":0.0},"to":{},"amount":0.0,"fromCurrency":{},"toCurrency":{},"exchangeRate":0.0}` + 

=== Response
*Content-Type*: `application/json`

==== `201 Created`
*Header*: `Location` + 

== `GET jersey-test/api/accounts/{accountId}/transfers/{transferId}`

=== Request
_No body_ + 
*Path Param*: accountId, java.lang.Long + 
*Path Param*: transferId, java.lang.Long + 

=== Response
*Content-Type*: `application/json`

==== `200 OK`
*Response Body*: (`com.github.madz0.jerseytest.model.Transfer`) + 
`application/json`: `{"amountInDestinationCurrency":0.0,"fromAccountId":0,"toAccountId":0,"originCurrency":"string","destinationCurrency":{},"roundedAmount":0.0,"exchangeDateAndTime":"date","from":{"currency":{},"amount":0.0},"to":{},"amount":0.0,"fromCurrency":{},"toCurrency":{},"exchangeRate":0.0}` + 

== `GET jersey-test/api/accounts/{id}`

=== Request
_No body_ + 
*Path Param*: id, java.lang.Long + 

=== Response
*Content-Type*: `application/json`

==== `200 OK`
*Response Body*: (`com.github.madz0.jerseytest.model.Account`) + 
`application/json`: `{"currency":"string","amount":0.0}` + 

== `PUT jersey-test/api/accounts/{id}`

=== Request
*Content-Type*: `application/json` + 
*Request Body*: (`com.github.madz0.jerseytest.model.Account`) + 
`application/json`: `{"currency":"string","amount":0.0}` + 
*Path Param*: id, java.lang.Long + 

=== Response
*Content-Type*: `application/json`

==== `200 OK`

== `GET jersey-test/api/accounts/{id}/transfers`

=== Request
_No body_ + 
*Path Param*: id, java.lang.Long + 
*Query Param*: page, int + 
*Query Param*: pageSize, int + 

=== Response
*Content-Type*: `application/json`

==== `200 OK`
*Response Body*: (`com.github.madz0.jerseytest.repository.Page<com.github.madz0.jerseytest.model.Transfer>`) + 
`application/json`: `{"contents":[{"amountInDestinationCurrency":0.0,"fromAccountId":0,"toAccountId":0,"originCurrency":"string","destinationCurrency":{},"roundedAmount":0.0,"exchangeDateAndTime":"date","from":{"currency":{},"amount":0.0},"to":{},"amount":0.0,"fromCurrency":{},"toCurrency":{},"exchangeRate":0.0}],"totalSize":0,"page":0,"pageSize":0}` + 

```

## Tests

Tests are split into unit tests and integration tests and both are configured 
for different phases.

Unit tests are run in `test` phase and integration tests in `integration-test` phase

So a simple `mvn install` will execute all the tests and will create an executable 
by `maven`'s `shade` plugin

Otherwise `mvn clean test` for unit tests and `mvn clean integration-test` to run
integration tests

After the run, a complete report will be generated by maven's `surefire` report. 

Here is the last executed sample so far:

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!-- Generated by Apache Maven Doxia Site Renderer 1.8.1 at 2019-08-05 -->
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>Surefire Report</title>
    <style type="text/css" media="all">
      @import url("./css/maven-base.css");
      @import url("./css/maven-theme.css");
      @import url("./css/site.css");
    </style>
    <link rel="stylesheet" href="./css/print.css" type="text/css" media="print" />
    <meta http-equiv="Content-Language" content="en" />
    
  </head>
  <body class="composite">
    <div id="banner">
      <div class="clear">
        <hr/>
      </div>
    </div>
    <div id="breadcrumbs">
      <div class="xleft">
        <span id="publishDate">Last Published: 2019-08-05</span>
          &nbsp;| <span id="projectVersion">Version: 1.0-SNAPSHOT</span>
      </div>
      <div class="xright">      </div>
      <div class="clear">
        <hr/>
      </div>
    </div>
    <div id="leftColumn">
      <div id="navcolumn">
      <a href="http://maven.apache.org/" title="Built by Maven" class="poweredBy">
        <img class="poweredBy" alt="Built by Maven" src="./images/logos/maven-feather.png" />
      </a>
      </div>
    </div>
    <div id="bodyColumn">
      <div id="contentBox">
<script type="application/javascript">
//<![CDATA[
function toggleDisplay(elementId) {
 var elm = document.getElementById(elementId + '-error');
 if (elm == null) {
  elm = document.getElementById(elementId + '-failure');
 }
 if (elm && typeof elm.style != "undefined") {
  if (elm.style.display == "none") {
   elm.style.display = "";
   document.getElementById(elementId + '-off').style.display = "none";
   document.getElementById(elementId + '-on').style.display = "inline";
  } else if (elm.style.display == "") {   elm.style.display = "none";
   document.getElementById(elementId + '-off').style.display = "inline";
   document.getElementById(elementId + '-on').style.display = "none";
  } 
 } 
 }
//]]>
</script>
<div class="section">
<h2><a name="Surefire_Report"></a>Surefire Report</h2></div>
<div class="section">
<h2><a name="Summary"></a>Summary</h2><a name="Summary"></a>
<p>[<a href="#Summary">Summary</a>] [<a href="#Package_List">Package List</a>] [<a href="#Test_Cases">Test Cases</a>]</p><br />
<table border="1" class="bodyTable">
<tr class="a">
<th>Tests</th>
<th>Errors</th>
<th>Failures</th>
<th>Skipped</th>
<th>Success Rate</th>
<th>Time</th></tr>
<tr class="b">
<td>99</td>
<td>0</td>
<td>0</td>
<td>0</td>
<td>100%</td>
<td>7.8</td></tr></table><br />
<p>Note: failures are anticipated and checked for with assertions while errors are unanticipated.</p><br /></div>
<div class="section">
<h2><a name="Package_List"></a>Package List</h2><a name="Package_List"></a>
<p>[<a href="#Summary">Summary</a>] [<a href="#Package_List">Package List</a>] [<a href="#Test_Cases">Test Cases</a>]</p><br />
<table border="1" class="bodyTable">
<tr class="a">
<th>Package</th>
<th>Tests</th>
<th>Errors</th>
<th>Failures</th>
<th>Skipped</th>
<th>Success Rate</th>
<th>Time</th></tr>
<tr class="b">
<td><a href="#com.github.madz0.jerseytest.repository">com.github.madz0.jerseytest.repository</a></td>
<td>17</td>
<td>0</td>
<td>0</td>
<td>0</td>
<td>100%</td>
<td>0.525</td></tr>
<tr class="a">
<td><a href="#com.github.madz0.jerseytest.resource">com.github.madz0.jerseytest.resource</a></td>
<td>44</td>
<td>0</td>
<td>0</td>
<td>0</td>
<td>100%</td>
<td>7.202</td></tr>
<tr class="b">
<td><a href="#com.github.madz0.jerseytest.service">com.github.madz0.jerseytest.service</a></td>
<td>38</td>
<td>0</td>
<td>0</td>
<td>0</td>
<td>100%</td>
<td>0.073</td></tr></table><br />
<p>Note: package statistics are not computed recursively, they only sum up all of its testsuites numbers.</p>
<div class="section">
<h3><a name="com.github.madz0.jerseytest.repository"></a>com.github.madz0.jerseytest.repository</h3><a name="com.github.madz0.jerseytest.repository"></a>
<table border="1" class="bodyTable">
<tr class="a">
<th></th>
<th>Class</th>
<th>Tests</th>
<th>Errors</th>
<th>Failures</th>
<th>Skipped</th>
<th>Success Rate</th>
<th>Time</th></tr>
<tr class="b">
<td><a href="#com.github.madz0.jerseytest.repository.AccountRepositoryImplUnitTest"><img src="images/icon_success_sml.gif" alt="" /></a></td>
<td><a href="#com.github.madz0.jerseytest.repository.AccountRepositoryImplUnitTest">AccountRepositoryImplUnitTest</a></td>
<td>6</td>
<td>0</td>
<td>0</td>
<td>0</td>
<td>100%</td>
<td>0.001</td></tr>
<tr class="a">
<td><a href="#com.github.madz0.jerseytest.repository.AbstractRepositoryUnitTest"><img src="images/icon_success_sml.gif" alt="" /></a></td>
<td><a href="#com.github.madz0.jerseytest.repository.AbstractRepositoryUnitTest">AbstractRepositoryUnitTest</a></td>
<td>5</td>
<td>0</td>
<td>0</td>
<td>0</td>
<td>100%</td>
<td>0.478</td></tr>
<tr class="b">
<td><a href="#com.github.madz0.jerseytest.repository.TransferRepositoryImplUnitTest"><img src="images/icon_success_sml.gif" alt="" /></a></td>
<td><a href="#com.github.madz0.jerseytest.repository.TransferRepositoryImplUnitTest">TransferRepositoryImplUnitTest</a></td>
<td>6</td>
<td>0</td>
<td>0</td>
<td>0</td>
<td>100%</td>
<td>0.046</td></tr></table></div>
<div class="section">
<h3><a name="com.github.madz0.jerseytest.resource"></a>com.github.madz0.jerseytest.resource</h3><a name="com.github.madz0.jerseytest.resource"></a>
<table border="1" class="bodyTable">
<tr class="a">
<th></th>
<th>Class</th>
<th>Tests</th>
<th>Errors</th>
<th>Failures</th>
<th>Skipped</th>
<th>Success Rate</th>
<th>Time</th></tr>
<tr class="b">
<td><a href="#com.github.madz0.jerseytest.resource.AccountsResourcePresentationIntegrationTest"><img src="images/icon_success_sml.gif" alt="" /></a></td>
<td><a href="#com.github.madz0.jerseytest.resource.AccountsResourcePresentationIntegrationTest">AccountsResourcePresentationIntegrationTest</a></td>
<td>4</td>
<td>0</td>
<td>0</td>
<td>0</td>
<td>100%</td>
<td>0.628</td></tr>
<tr class="a">
<td><a href="#com.github.madz0.jerseytest.resource.AccountsResourceValidationResponseIntegrationTest"><img src="images/icon_success_sml.gif" alt="" /></a></td>
<td><a href="#com.github.madz0.jerseytest.resource.AccountsResourceValidationResponseIntegrationTest">AccountsResourceValidationResponseIntegrationTest</a></td>
<td>26</td>
<td>0</td>
<td>0</td>
<td>0</td>
<td>100%</td>
<td>2.693</td></tr>
<tr class="b">
<td><a href="#com.github.madz0.jerseytest.resource.AccountsResourceSuccessIntegrationTest"><img src="images/icon_success_sml.gif" alt="" /></a></td>
<td><a href="#com.github.madz0.jerseytest.resource.AccountsResourceSuccessIntegrationTest">AccountsResourceSuccessIntegrationTest</a></td>
<td>7</td>
<td>0</td>
<td>0</td>
<td>0</td>
<td>100%</td>
<td>0.854</td></tr>
<tr class="a">
<td><a href="#com.github.madz0.jerseytest.resource.AccountsResourceExceptionsResponseIntegrationTest"><img src="images/icon_success_sml.gif" alt="" /></a></td>
<td><a href="#com.github.madz0.jerseytest.resource.AccountsResourceExceptionsResponseIntegrationTest">AccountsResourceExceptionsResponseIntegrationTest</a></td>
<td>7</td>
<td>0</td>
<td>0</td>
<td>0</td>
<td>100%</td>
<td>3.027</td></tr></table></div>
<div class="section">
<h3><a name="com.github.madz0.jerseytest.service"></a>com.github.madz0.jerseytest.service</h3><a name="com.github.madz0.jerseytest.service"></a>
<table border="1" class="bodyTable">
<tr class="a">
<th></th>
<th>Class</th>
<th>Tests</th>
<th>Errors</th>
<th>Failures</th>
<th>Skipped</th>
<th>Success Rate</th>
<th>Time</th></tr>
<tr class="b">
<td><a href="#com.github.madz0.jerseytest.service.AccountServiceUnitTest"><img src="images/icon_success_sml.gif" alt="" /></a></td>
<td><a href="#com.github.madz0.jerseytest.service.AccountServiceUnitTest">AccountServiceUnitTest</a></td>
<td>38</td>
<td>0</td>
<td>0</td>
<td>0</td>
<td>100%</td>
<td>0.073</td></tr></table></div><br /></div>
<div class="section">
<h2><a name="Test_Cases"></a>Test Cases</h2><a name="Test_Cases"></a>
<p>[<a href="#Summary">Summary</a>] [<a href="#Package_List">Package List</a>] [<a href="#Test_Cases">Test Cases</a>]</p>
<div class="section">
<h3><a name="AccountRepositoryImplUnitTest"></a>AccountRepositoryImplUnitTest</h3><a name="com.github.madz0.jerseytest.repository.AccountRepositoryImplUnitTest"></a>
<table border="1" class="bodyTable">
<tr class="a">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.repository.AccountRepositoryImplUnitTest.findAll_wrongPageSize_shouldThrowIllegalArgumentException"></a>findAll_wrongPageSize_shouldThrowIllegalArgumentException</td>
<td>0</td></tr>
<tr class="b">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.repository.AccountRepositoryImplUnitTest.findAll"></a>findAll</td>
<td>0.001</td></tr>
<tr class="a">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.repository.AccountRepositoryImplUnitTest.findById"></a>findById</td>
<td>0</td></tr>
<tr class="b">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.repository.AccountRepositoryImplUnitTest.findForUpdateById_nullId_shouldThrowIllegalArgumentException"></a>findForUpdateById_nullId_shouldThrowIllegalArgumentException</td>
<td>0</td></tr>
<tr class="a">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.repository.AccountRepositoryImplUnitTest.findAll_biggerPageThanTotalSize_shouldThrowIllegalArgumentException"></a>findAll_biggerPageThanTotalSize_shouldThrowIllegalArgumentException</td>
<td>0</td></tr>
<tr class="b">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.repository.AccountRepositoryImplUnitTest.findByIdWith_nullId_shouldThrowIllegalArgumentException"></a>findByIdWith_nullId_shouldThrowIllegalArgumentException</td>
<td>0</td></tr></table></div>
<div class="section">
<h3><a name="AccountsResourcePresentationIntegrationTest"></a>AccountsResourcePresentationIntegrationTest</h3><a name="com.github.madz0.jerseytest.resource.AccountsResourcePresentationIntegrationTest"></a>
<table border="1" class="bodyTable">
<tr class="a">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.resource.AccountsResourcePresentationIntegrationTest.get_whenProperRequest_thenOkAndMoneyShouldBeRounded"></a>get_whenProperRequest_thenOkAndMoneyShouldBeRounded</td>
<td>0.14</td></tr>
<tr class="b">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.resource.AccountsResourcePresentationIntegrationTest.getTransfer_whenProperRequest_thenOkAndMoneyShouldBeRounded"></a>getTransfer_whenProperRequest_thenOkAndMoneyShouldBeRounded</td>
<td>0.126</td></tr>
<tr class="a">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.resource.AccountsResourcePresentationIntegrationTest.list_whenProperRequest_thenOkAndMoneyShouldBeRounded"></a>list_whenProperRequest_thenOkAndMoneyShouldBeRounded</td>
<td>0.148</td></tr>
<tr class="b">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.resource.AccountsResourcePresentationIntegrationTest.transferList_whenProperRequest_thenOkAndMoneyShouldBeRounded"></a>transferList_whenProperRequest_thenOkAndMoneyShouldBeRounded</td>
<td>0.208</td></tr></table></div>
<div class="section">
<h3><a name="AbstractRepositoryUnitTest"></a>AbstractRepositoryUnitTest</h3><a name="com.github.madz0.jerseytest.repository.AbstractRepositoryUnitTest"></a>
<table border="1" class="bodyTable">
<tr class="a">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.repository.AbstractRepositoryUnitTest.save_nullEntity_shouldThrow"></a>save_nullEntity_shouldThrow</td>
<td>0.454</td></tr>
<tr class="b">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.repository.AbstractRepositoryUnitTest.save_wrongEntityId_shouldThrow"></a>save_wrongEntityId_shouldThrow</td>
<td>0.001</td></tr>
<tr class="a">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.repository.AbstractRepositoryUnitTest.save"></a>save</td>
<td>0.01</td></tr>
<tr class="b">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.repository.AbstractRepositoryUnitTest.saveMerge_whenEntityExists_MergeSuccesfully"></a>saveMerge_whenEntityExists_MergeSuccesfully</td>
<td>0.003</td></tr>
<tr class="a">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.repository.AbstractRepositoryUnitTest.saveMerge_whenEntityDoesNotExist_throwDataIntegrityException"></a>saveMerge_whenEntityDoesNotExist_throwDataIntegrityException</td>
<td>0.003</td></tr></table></div>
<div class="section">
<h3><a name="AccountServiceUnitTest"></a>AccountServiceUnitTest</h3><a name="com.github.madz0.jerseytest.service.AccountServiceUnitTest"></a>
<table border="1" class="bodyTable">
<tr class="a">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.service.AccountServiceUnitTest.makeTransferNullTransferObjectTest_shouldThrowsIllegalArg"></a>makeTransferNullTransferObjectTest_shouldThrowsIllegalArg</td>
<td>0.001</td></tr>
<tr class="b">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.service.AccountServiceUnitTest.updateEntityWithLargerMoneyThanWeSupported_shouldThrowsIllegalArgException"></a>updateEntityWithLargerMoneyThanWeSupported_shouldThrowsIllegalArgException</td>
<td>0.001</td></tr>
<tr class="a">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.service.AccountServiceUnitTest.createEntityWithNullCurrency_shouldThrowsIllegalArgException"></a>createEntityWithNullCurrency_shouldThrowsIllegalArgException</td>
<td>0</td></tr>
<tr class="b">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.service.AccountServiceUnitTest.makeTransferNullTransferReceiverIdTest_shouldThrowsIllegalArg"></a>makeTransferNullTransferReceiverIdTest_shouldThrowsIllegalArg</td>
<td>0.001</td></tr>
<tr class="a">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.service.AccountServiceUnitTest.updateEntityWithWrongVersionTest_shouldThrowsIllegalArgException"></a>updateEntityWithWrongVersionTest_shouldThrowsIllegalArgException</td>
<td>0.001</td></tr>
<tr class="b">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.service.AccountServiceUnitTest.createEntityWithLargerMoneyThanWeSupported_shouldThrowsIllegalArgException"></a>createEntityWithLargerMoneyThanWeSupported_shouldThrowsIllegalArgException</td>
<td>0</td></tr>
<tr class="a">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.service.AccountServiceUnitTest.updateNullEntityTest_shouldThrowsIllegalArgException"></a>updateNullEntityTest_shouldThrowsIllegalArgException</td>
<td>0</td></tr>
<tr class="b">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.service.AccountServiceUnitTest.createEntityWithNotNullVersionTest_shouldThrowsIllegalArgException"></a>createEntityWithNotNullVersionTest_shouldThrowsIllegalArgException</td>
<td>0.001</td></tr>
<tr class="a">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.service.AccountServiceUnitTest.makeTransferWithZeroAmountTest_shouldThrowsIllegalArg"></a>makeTransferWithZeroAmountTest_shouldThrowsIllegalArg</td>
<td>0</td></tr>
<tr class="b">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.service.AccountServiceUnitTest.getAccountByIdWrongIdTest_shouldReceivesIllegalArgException"></a>getAccountByIdWrongIdTest_shouldReceivesIllegalArgException</td>
<td>0</td></tr>
<tr class="a">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.service.AccountServiceUnitTest.makeTransferWithBiggerFractionThanWeSupportTest_shouldThrowsIllegalArg"></a>makeTransferWithBiggerFractionThanWeSupportTest_shouldThrowsIllegalArg</td>
<td>0.001</td></tr>
<tr class="b">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.service.AccountServiceUnitTest.findAllWithWrongPageSizeTest_shouldThrowIllegalArgException"></a>findAllWithWrongPageSizeTest_shouldThrowIllegalArgException</td>
<td>0</td></tr>
<tr class="a">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.service.AccountServiceUnitTest.makeTransferNullTransferSenderIdTest_shouldThrowsIllegalArg"></a>makeTransferNullTransferSenderIdTest_shouldThrowsIllegalArg</td>
<td>0</td></tr>
<tr class="b">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.service.AccountServiceUnitTest.makeTransferWithBiggerDigitsThanWeSupportTest_shouldThrowsIllegalArg"></a>makeTransferWithBiggerDigitsThanWeSupportTest_shouldThrowsIllegalArg</td>
<td>0.001</td></tr>
<tr class="a">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.service.AccountServiceUnitTest.updateTest"></a>updateTest</td>
<td>0.013</td></tr>
<tr class="b">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.service.AccountServiceUnitTest.updateEntityWithNullIdTest_shouldThrowsIllegalArgException"></a>updateEntityWithNullIdTest_shouldThrowsIllegalArgException</td>
<td>0.001</td></tr>
<tr class="a">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.service.AccountServiceUnitTest.findAllTest"></a>findAllTest</td>
<td>0.001</td></tr>
<tr class="b">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.service.AccountServiceUnitTest.createNullEntityTest_shouldThrowsIllegalArgException"></a>createNullEntityTest_shouldThrowsIllegalArgException</td>
<td>0</td></tr>
<tr class="a">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.service.AccountServiceUnitTest.findAllWithWrongPageTest_shouldThrowIllegalArgException"></a>findAllWithWrongPageTest_shouldThrowIllegalArgException</td>
<td>0.001</td></tr>
<tr class="b">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.service.AccountServiceUnitTest.makeTransferWithNullAmountTest_shouldThrowsIllegalArg"></a>makeTransferWithNullAmountTest_shouldThrowsIllegalArg</td>
<td>0</td></tr>
<tr class="a">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.service.AccountServiceUnitTest.createEntityWithNotNullIdTest_shouldThrowsIllegalArgException"></a>createEntityWithNotNullIdTest_shouldThrowsIllegalArgException</td>
<td>0.001</td></tr>
<tr class="b">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.service.AccountServiceUnitTest.makeTransferWithSmallerThanZeroAmountTest_shouldThrowsIllegalArg"></a>makeTransferWithSmallerThanZeroAmountTest_shouldThrowsIllegalArg</td>
<td>0</td></tr>
<tr class="a">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.service.AccountServiceUnitTest.makeTransferWithTooBigExchangeRateTest_shouldThrowExternalServiceException"></a>makeTransferWithTooBigExchangeRateTest_shouldThrowExternalServiceException</td>
<td>0.011</td></tr>
<tr class="b">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.service.AccountServiceUnitTest.makeTransferTest"></a>makeTransferTest</td>
<td>0.012</td></tr>
<tr class="a">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.service.AccountServiceUnitTest.findTransferByIdTest"></a>findTransferByIdTest</td>
<td>0.001</td></tr>
<tr class="b">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.service.AccountServiceUnitTest.findAllTransfersByAccountIdEmptyResultTest"></a>findAllTransfersByAccountIdEmptyResultTest</td>
<td>0.001</td></tr>
<tr class="a">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.service.AccountServiceUnitTest.makeTransferWithInvalidReceiverTest_shouldThrowsIllegalArg"></a>makeTransferWithInvalidReceiverTest_shouldThrowsIllegalArg</td>
<td>0.001</td></tr>
<tr class="b">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.service.AccountServiceUnitTest.makeTransferFromAnAccountToItselfTest_shouldThrowsIllegalArg"></a>makeTransferFromAnAccountToItselfTest_shouldThrowsIllegalArg</td>
<td>0.001</td></tr>
<tr class="a">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.service.AccountServiceUnitTest.findAllTransfersByAccountIdTest"></a>findAllTransfersByAccountIdTest</td>
<td>0.002</td></tr>
<tr class="b">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.service.AccountServiceUnitTest.makeTransferWithBigAmountAccountTest_shouldThrowUnsupportedOperation"></a>makeTransferWithBigAmountAccountTest_shouldThrowUnsupportedOperation</td>
<td>0.002</td></tr>
<tr class="a">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.service.AccountServiceUnitTest.createTest"></a>createTest</td>
<td>0.001</td></tr>
<tr class="b">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.service.AccountServiceUnitTest.makeTransferWhenExternalServiceFailsTest_shouldThrowExternalServiceException"></a>makeTransferWhenExternalServiceFailsTest_shouldThrowExternalServiceException</td>
<td>0.001</td></tr>
<tr class="a">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.service.AccountServiceUnitTest.getAccountByIdTest"></a>getAccountByIdTest</td>
<td>0.001</td></tr>
<tr class="b">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.service.AccountServiceUnitTest.updateTest_whenEntityDoesNotExist_throwsDataIntegrityException"></a>updateTest_whenEntityDoesNotExist_throwsDataIntegrityException</td>
<td>0.002</td></tr>
<tr class="a">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.service.AccountServiceUnitTest.makeTransferWithInsufficientMoneyInSenderAccountTest_shouldThrowsIllegalArg"></a>makeTransferWithInsufficientMoneyInSenderAccountTest_shouldThrowsIllegalArg</td>
<td>0.001</td></tr>
<tr class="b">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.service.AccountServiceUnitTest.makeTransferWithInvalidSenderTest_shouldThrowsIllegalArg"></a>makeTransferWithInvalidSenderTest_shouldThrowsIllegalArg</td>
<td>0.001</td></tr>
<tr class="a">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.service.AccountServiceUnitTest.updateEntityWithBadIdTest_shouldThrowsIllegalArgException"></a>updateEntityWithBadIdTest_shouldThrowsIllegalArgException</td>
<td>0</td></tr>
<tr class="b">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.service.AccountServiceUnitTest.updateEntity_whenCurrencyIsNull_shouldStillWorks"></a>updateEntity_whenCurrencyIsNull_shouldStillWorks</td>
<td>0.001</td></tr></table></div>
<div class="section">
<h3><a name="AccountsResourceValidationResponseIntegrationTest"></a>AccountsResourceValidationResponseIntegrationTest</h3><a name="com.github.madz0.jerseytest.resource.AccountsResourceValidationResponseIntegrationTest"></a>
<table border="1" class="bodyTable">
<tr class="a">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.resource.AccountsResourceValidationResponseIntegrationTest.update_whenPostTooBigAmount_thenBadRequest"></a>update_whenPostTooBigAmount_thenBadRequest</td>
<td>0.139</td></tr>
<tr class="b">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.resource.AccountsResourceValidationResponseIntegrationTest.request_whenPathIsNotExists_thenNotFound"></a>request_whenPathIsNotExists_thenNotFound</td>
<td>0.099</td></tr>
<tr class="a">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.resource.AccountsResourceValidationResponseIntegrationTest.transfer_whenFromIsNull_thenBadRequest"></a>transfer_whenFromIsNull_thenBadRequest</td>
<td>0.118</td></tr>
<tr class="b">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.resource.AccountsResourceValidationResponseIntegrationTest.update_whenPostNullAmount_thenBadRequest"></a>update_whenPostNullAmount_thenBadRequest</td>
<td>0.095</td></tr>
<tr class="a">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.resource.AccountsResourceValidationResponseIntegrationTest.transfer_whenMoneyFractionIsMuch_thenBadRequest"></a>transfer_whenMoneyFractionIsMuch_thenBadRequest</td>
<td>0.109</td></tr>
<tr class="b">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.resource.AccountsResourceValidationResponseIntegrationTest.create_whenPostTooBigAmountFraction_thenBadRequest"></a>create_whenPostTooBigAmountFraction_thenBadRequest</td>
<td>0.112</td></tr>
<tr class="a">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.resource.AccountsResourceValidationResponseIntegrationTest.transferGet_whenTransferIdLessThanZero_thenBadRequest"></a>transferGet_whenTransferIdLessThanZero_thenBadRequest</td>
<td>0.092</td></tr>
<tr class="b">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.resource.AccountsResourceValidationResponseIntegrationTest.create_whenPostNullCurrency_thenBadRequest"></a>create_whenPostNullCurrency_thenBadRequest</td>
<td>0.124</td></tr>
<tr class="a">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.resource.AccountsResourceValidationResponseIntegrationTest.transfer_whenAmountIsZero_thenBadRequest"></a>transfer_whenAmountIsZero_thenBadRequest</td>
<td>0.112</td></tr>
<tr class="b">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.resource.AccountsResourceValidationResponseIntegrationTest.transfer_whenAmountIsNull_thenBadRequest"></a>transfer_whenAmountIsNull_thenBadRequest</td>
<td>0.117</td></tr>
<tr class="a">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.resource.AccountsResourceValidationResponseIntegrationTest.create_whenCurrencyIsNotValidRequest_thenRespondBadRequest"></a>create_whenCurrencyIsNotValidRequest_thenRespondBadRequest</td>
<td>0.125</td></tr>
<tr class="b">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.resource.AccountsResourceValidationResponseIntegrationTest.update_whenPostNullVersion_thenBadRequest"></a>update_whenPostNullVersion_thenBadRequest</td>
<td>0.102</td></tr>
<tr class="a">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.resource.AccountsResourceValidationResponseIntegrationTest.transfer_whenToIsNull_thenBadRequest"></a>transfer_whenToIsNull_thenBadRequest</td>
<td>0.108</td></tr>
<tr class="b">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.resource.AccountsResourceValidationResponseIntegrationTest.request_whenMethodIsWrong_thenMethodNotAllowed"></a>request_whenMethodIsWrong_thenMethodNotAllowed</td>
<td>0.079</td></tr>
<tr class="a">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.resource.AccountsResourceValidationResponseIntegrationTest.transferGet_whenAccountIdLessThanZero_thenBadRequest"></a>transferGet_whenAccountIdLessThanZero_thenBadRequest</td>
<td>0.095</td></tr>
<tr class="b">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.resource.AccountsResourceValidationResponseIntegrationTest.transfer_whenMoneyIsTooBig_thenBadRequest"></a>transfer_whenMoneyIsTooBig_thenBadRequest</td>
<td>0.11</td></tr>
<tr class="a">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.resource.AccountsResourceValidationResponseIntegrationTest.create_whenPostZeroAmount_thenBadRequest"></a>create_whenPostZeroAmount_thenBadRequest</td>
<td>0.085</td></tr>
<tr class="b">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.resource.AccountsResourceValidationResponseIntegrationTest.update_whenPostNegativeAmount_thenBadRequest"></a>update_whenPostNegativeAmount_thenBadRequest</td>
<td>0.108</td></tr>
<tr class="a">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.resource.AccountsResourceValidationResponseIntegrationTest.create_whenJsonDataIsMalformedRequest_thenRespondBadRequest"></a>create_whenJsonDataIsMalformedRequest_thenRespondBadRequest</td>
<td>0.085</td></tr>
<tr class="b">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.resource.AccountsResourceValidationResponseIntegrationTest.transferList_whenIdLessThanZero_thenBadRequest"></a>transferList_whenIdLessThanZero_thenBadRequest</td>
<td>0.097</td></tr>
<tr class="a">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.resource.AccountsResourceValidationResponseIntegrationTest.update_whenPostZeroAmount_thenBadRequest"></a>update_whenPostZeroAmount_thenBadRequest</td>
<td>0.096</td></tr>
<tr class="b">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.resource.AccountsResourceValidationResponseIntegrationTest.transfer_whenAmountIsNegative_thenBadRequest"></a>transfer_whenAmountIsNegative_thenBadRequest</td>
<td>0.122</td></tr>
<tr class="a">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.resource.AccountsResourceValidationResponseIntegrationTest.create_whenPostNegativeAmount_thenBadRequest"></a>create_whenPostNegativeAmount_thenBadRequest</td>
<td>0.075</td></tr>
<tr class="b">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.resource.AccountsResourceValidationResponseIntegrationTest.update_whenPostTooBigAmountFraction_thenBadRequest"></a>update_whenPostTooBigAmountFraction_thenBadRequest</td>
<td>0.073</td></tr>
<tr class="a">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.resource.AccountsResourceValidationResponseIntegrationTest.create_whenPostNullAmount_thenBadRequest"></a>create_whenPostNullAmount_thenBadRequest</td>
<td>0.088</td></tr>
<tr class="b">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.resource.AccountsResourceValidationResponseIntegrationTest.create_whenPostTooBigAmount_thenBadRequest"></a>create_whenPostTooBigAmount_thenBadRequest</td>
<td>0.097</td></tr></table></div>
<div class="section">
<h3><a name="TransferRepositoryImplUnitTest"></a>TransferRepositoryImplUnitTest</h3><a name="com.github.madz0.jerseytest.repository.TransferRepositoryImplUnitTest"></a>
<table border="1" class="bodyTable">
<tr class="a">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.repository.TransferRepositoryImplUnitTest.findAll_ExceptionInCountQuery_shouldThrowDbQueryException"></a>findAll_ExceptionInCountQuery_shouldThrowDbQueryException</td>
<td>0.035</td></tr>
<tr class="b">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.repository.TransferRepositoryImplUnitTest.findTransferByAccountIdAndIdTest"></a>findTransferByAccountIdAndIdTest</td>
<td>0.001</td></tr>
<tr class="a">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.repository.TransferRepositoryImplUnitTest.findAllByAccountId"></a>findAllByAccountId</td>
<td>0.001</td></tr>
<tr class="b">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.repository.TransferRepositoryImplUnitTest.findAll_ExceptionInListQuery_shouldThrowDbQueryException"></a>findAll_ExceptionInListQuery_shouldThrowDbQueryException</td>
<td>0.003</td></tr>
<tr class="a">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.repository.TransferRepositoryImplUnitTest.findAll_wrongPageSize_shouldThrowIllegalArgumentException"></a>findAll_wrongPageSize_shouldThrowIllegalArgumentException</td>
<td>0.001</td></tr>
<tr class="b">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.repository.TransferRepositoryImplUnitTest.findTransferByAccountIdAndId_whenIdsAreNull_illegalException"></a>findTransferByAccountIdAndId_whenIdsAreNull_illegalException</td>
<td>0.002</td></tr></table></div>
<div class="section">
<h3><a name="AccountsResourceSuccessIntegrationTest"></a>AccountsResourceSuccessIntegrationTest</h3><a name="com.github.madz0.jerseytest.resource.AccountsResourceSuccessIntegrationTest"></a>
<table border="1" class="bodyTable">
<tr class="a">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.resource.AccountsResourceSuccessIntegrationTest.update_whenProperRequest_thenOk"></a>update_whenProperRequest_thenOk</td>
<td>0.14</td></tr>
<tr class="b">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.resource.AccountsResourceSuccessIntegrationTest.create_whenCorrectRequest_thenCreatedAndContainsLocationHeader"></a>create_whenCorrectRequest_thenCreatedAndContainsLocationHeader</td>
<td>0.128</td></tr>
<tr class="a">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.resource.AccountsResourceSuccessIntegrationTest.transferList_whenProperRequest_thenOkAndContainsPageData"></a>transferList_whenProperRequest_thenOkAndContainsPageData</td>
<td>0.129</td></tr>
<tr class="b">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.resource.AccountsResourceSuccessIntegrationTest.list_whenProperRequest_thenOkAndContainsPageData"></a>list_whenProperRequest_thenOkAndContainsPageData</td>
<td>0.124</td></tr>
<tr class="a">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.resource.AccountsResourceSuccessIntegrationTest.get_whenProperRequest_thenOkAndContainsResourceData"></a>get_whenProperRequest_thenOkAndContainsResourceData</td>
<td>0.095</td></tr>
<tr class="b">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.resource.AccountsResourceSuccessIntegrationTest.getTransfer_whenProperRequest_thenOkAndContainsResourceData"></a>getTransfer_whenProperRequest_thenOkAndContainsResourceData</td>
<td>0.102</td></tr>
<tr class="a">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.resource.AccountsResourceSuccessIntegrationTest.transfer"></a>transfer</td>
<td>0.133</td></tr></table></div>
<div class="section">
<h3><a name="AccountsResourceExceptionsResponseIntegrationTest"></a>AccountsResourceExceptionsResponseIntegrationTest</h3><a name="com.github.madz0.jerseytest.resource.AccountsResourceExceptionsResponseIntegrationTest"></a>
<table border="1" class="bodyTable">
<tr class="a">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.resource.AccountsResourceExceptionsResponseIntegrationTest.create_whenThrowsRestIllegalArgException_thenBadRequest"></a>create_whenThrowsRestIllegalArgException_thenBadRequest</td>
<td>1.633</td></tr>
<tr class="b">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.resource.AccountsResourceExceptionsResponseIntegrationTest.transfer_whenUncaughtException_thenInternalServerErrorAndContainsErrorCode"></a>transfer_whenUncaughtException_thenInternalServerErrorAndContainsErrorCode</td>
<td>0.237</td></tr>
<tr class="a">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.resource.AccountsResourceExceptionsResponseIntegrationTest.list_whenThrowsRestUnsupportedException_thenBadRequest"></a>list_whenThrowsRestUnsupportedException_thenBadRequest</td>
<td>0.155</td></tr>
<tr class="b">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.resource.AccountsResourceExceptionsResponseIntegrationTest.update_ConcurrentModificationEx_thenConflict"></a>update_ConcurrentModificationEx_thenConflict</td>
<td>0.15</td></tr>
<tr class="a">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.resource.AccountsResourceExceptionsResponseIntegrationTest.transfer_whenDbQueryException_thenInternalServerErrorAndContainsErrorCode"></a>transfer_whenDbQueryException_thenInternalServerErrorAndContainsErrorCode</td>
<td>0.17</td></tr>
<tr class="b">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.resource.AccountsResourceExceptionsResponseIntegrationTest.getTransfer_whenDataIntegrationException_thenNotFound"></a>getTransfer_whenDataIntegrationException_thenNotFound</td>
<td>0.127</td></tr>
<tr class="a">
<td><img src="images/icon_success_sml.gif" alt="" /></td>
<td><a name="TC_com.github.madz0.jerseytest.resource.AccountsResourceExceptionsResponseIntegrationTest.get_whenDataIntegrationException_thenNotFound"></a>get_whenDataIntegrationException_thenNotFound</td>
<td>0.098</td></tr></table></div><br /></div>
      </div>
    </div>
    <div class="clear">
      <hr/>
    </div>
    <div id="footer">
      <div class="xright">
        Copyright &#169;      2019.All rights reserved.      </div>
      <div class="clear">
        <hr/>
      </div>
    </div>
  </body>
</html>  
