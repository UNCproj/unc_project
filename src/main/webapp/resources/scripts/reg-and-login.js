(function () {
	var app = angular.module('regAndLogin', ['registration', 'login']);

	app.controller('ralController', function() {
		this.isRegistred = true;

		this.setRegistred = function(value) {
			this.isRegistred = value;	
		};
	});
})();