var Account = function() {
};
Account.prototype = {
	headers : {},
	divMessage : null,
	inputId : null,
	inputName : null,
	inputVersion : null,
	init : function() {
		var csrfHeader = $("meta[name='_csrf_header']").attr('content');
		var csrfToken = $("meta[name='_csrf']").attr('content');
		this.headers[csrfHeader] = csrfToken;

		this.divMessage = $('div#message');
		this.inputId = $('input#id');
		this.inputName = $('input#name');
		this.inputVersion = $('input#version');
		
		this.inputId.click(function() {
			$(this).css('background-color', '');
		});
		this.inputName.click(function() {
			$(this).css('background-color', '');
		});

		var self = this;
		$('#button_clear').click(function() {
			self.clear();
		});
		$('#button_find').click(function() {
			self.find();
		});
		$('#button_put').click(function() {
			self.put();
		});
		$('#button_max').click(function() {
			self.max();
		});
		$('#button_sysgc').click(function() {
			self.sysgc();
		});
	},
	clear : function() {
		this.divMessage.text('');
		this.inputId.val('');
		this.inputName.val('');
		this.inputVersion.val('');
		this.inputId.css('background-color', '');
		this.inputName.css('background-color', '');
	},
	set : function(account, message) {
		this.divMessage.html('<span>' + message + '</span>');
		this.inputId.val(account.id);
		this.inputName.val(account.name);
		this.inputVersion.val(account.version);
	},
	find : function() {
		var id = this.inputId.val();
		this.clear();
		var self = this;
		$.getJSON('/account/' + id, function(result, message, status) {
			self.set(result, message);
		}).fail(function(result, message, status) {
			self.divMessage.text(message);
		});
	},
	put : function() {
		var data = {};
		data['id'] = this.inputId.val();
		data['name'] = this.inputName.val();
		data['version'] = this.inputVersion.val();
		this.clear();
		var self = this;
		$.ajax({
			url : '/account/put',
			type : 'PUT',
			contentType : 'application/json;charset=UTF-8',
			data : JSON.stringify(data),
			dataType : 'json',
			headers : self.headers,
			success : function(result, message, status) {
				self.set(result, message);
			}
		}).fail(function(result, message, status) {
			result.responseJSON.forEach(function(error){
				$('input#' + error.field).css('background-color', 'red');
				self.divMessage.append('<span class="error">' + error.defaultMessage + '</span>');
			});
		});
	},
	max : function() {
		this.clear();
		var self = this;
		$.getJSON('/account/max', function(result, message, status) {
			self.set(result, message);
			alert(JSON.stringify(result));
			alert(JSON.stringify(message));
			alert(JSON.stringify(status));
		}).fail(function(result, message, status) {
//			self.divMessage.text(result.responseJSON);
			alert(JSON.stringify(result));
			alert(JSON.stringify(message));
			alert(JSON.stringify(status));
		});
	},
	sysgc : function() {
		this.clear();
		var self = this;
		$.getJSON('/account/sysgc', function(result, message, status) {
			alert('success');
			alert(JSON.stringify(result));
			alert(JSON.stringify(message));
			alert(JSON.stringify(status));
		}).done(function(result, message, status) {
			alert('done');
			self.divMessage.text(result.responseJSON);
			alert(JSON.stringify(result));
			alert(JSON.stringify(message));
			alert(JSON.stringify(status));
		}).fail(function(result, message, status) {
			alert('fail');
			self.divMessage.text(result.responseJSON);
			alert(JSON.stringify(result));
			alert(JSON.stringify(message));
			alert(JSON.stringify(status));
		});
	}
};
