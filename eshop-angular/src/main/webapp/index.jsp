<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" session="false" %>
<!DOCTYPE html>
<html lang="en" >
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>eShop AngularJS</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" crossorigin="anonymous">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css"  crossorigin="anonymous">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.6/angular.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.6/angular-resource.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.6/angular-route.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="${pageContext.request.contextPath}/angular_app.js"></script>
</head>
<body>
<!-- navigation bar -->
<nav class="navbar navbar-inverse navbar-static-top">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="${pageContext.request.contextPath}/">PA165 AngularJS eShop</a>
        </div>
        <div id="navbar" class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <li><a href="#!/shopping">Go shopping</a></li>
            </ul>
        </div><!--/.nav-collapse -->
    </div>
</nav>

<div class="container">
    <%--a DIV element with the ng-app attribute marking the place managed by AngularJS--%>
    <div ng-app="pa165eshopApp"><!-- AngularJS takes care of this element -->
        <%--a DIV with ng-view attribute which is a placeholder for changing HTML views--%>
        <div ng-view></div><!-- the place where HTML templates are replaced by AngularJS routing -->
    </div>

    <footer class="footer">
        <p>&copy;&nbsp;Masaryk University</p>
    </footer>
</div>
</body>
</html>