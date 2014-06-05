'use strict';

angular.module('bblorganizer', [
		'ngRoute',
		'ngTable',
		'raaj-message-interceptor',
		'raaj-message-service',
		'raaj-progress-interceptor',
	])
	.config(function (raajProgressInterceptorProvider) {
		raajProgressInterceptorProvider.setDefaultMessage('Chargement en cours...');
	})
	.config(function (raajMessageServiceProvider) {
		raajMessageServiceProvider.setBootstrapVersion('3.x');
	})
;