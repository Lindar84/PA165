'use strict';

/* Defines application and its dependencies */

// ngRoute provides the functionality of mapping URL fragments to HTML views
var pa165eshopApp = angular.module('pa165eshopApp', ['ngRoute', 'eshopControllers']);
// eshopControllers implements our application
var eshopControllers = angular.module('eshopControllers', []);

/* Configures URL fragment routing, e.g. #/product/1  */
pa165eshopApp.config(['$routeProvider',
    function ($routeProvider) {
        $routeProvider.
        when('/shopping', {templateUrl: 'partials/shopping.html', controller: 'ShoppingCtrl'}).
        when('/product/:productId', {templateUrl: 'partials/product_detail.html', controller: 'ProductDetailCtrl'}).
        otherwise({redirectTo: '/shopping'});
    }]);

/*
 * Public eshop interface
 */

// helper procedure loading products to category
function loadCategoryProducts($http, category, prodLink) {
    $http.get(prodLink).then(function (response) {
        category.products = response.data['_embedded']['products'];
        console.log('AJAX loaded ' + category.products.length + ' products to category ' + category.name);
    });
}

/*
 * Shopping page with all categories and products
 * ShoppingCtrl makes AJAX calls for the list of categories and then for the list of products in each category,
 * the results are stored in $scope.categories
 */
eshopControllers.controller('ShoppingCtrl', function ($scope, $http) {

    console.log('calling  /eshop/api/v1/categories/');
    $http.get('/eshop/api/v1/categories/').then(function (response) {

        var categories = response.data['_embedded']['categories'];
        console.log('AJAX loaded all categories');
        $scope.categories = categories;
        for (var i = 0; i < categories.length; i++) {
            var category = categories[i];
            var categoryProductsLink = category['_links'].products.href;
            loadCategoryProducts($http, category, categoryProductsLink);
        }
    });
});

/*
 * Product detail page
 * ProductDetailCtrl makes a single AJAX call to a product detail and stores the result in $scope.product
 */
eshopControllers.controller('ProductDetailCtrl', function ($scope, $routeParams, $http) {

        // get product id from URL fragment #/product/:productId
        var productId = $routeParams.productId;
        $http.get('/eshop/api/v1/products/' + productId).then(function (response) {

            $scope.product = response.data;
            console.log('AJAX loaded detail of product ' + $scope.product.name);
        });
    });

// loads category data from the REST API
eshopControllers.controller('CategoryDetailCtrl', ['$scope', '$routeParams', '$http',
    function ($scope, $routeParams, $http) {
    console.log('calling /eshop/api/v1/categories/:categoryId');

    var categoryId = $routeParams.categoryId;
    $http.get('/eshop/api/v1/categories/' + categoryId).then(function (response) {
        var category = response.data;
        $scope.category = category;
        //$scope.category = response.data;
        console.log('AJAX loaded detail of category ' + category.name);
        loadCategoryProducts($http, category, category['_links'].products.href);
    })
}]);