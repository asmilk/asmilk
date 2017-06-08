<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<sec:csrfMetaTags />
<title><fmt:message key="account.title" /></title>
<link type="image/x-icon" rel="shortcut icon" href="/favicon.ico" />
<link type="text/css" rel="stylesheet" href="/themes/icon.css" />
<link type="text/css" rel="stylesheet" href="/themes/default/easyui.css" />
<link type="text/css" rel="stylesheet" href="/style/demo.css" />
<script type="text/javascript" src="/script/jquery.min.js"></script>
<script type="text/javascript" src="/script/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">
$(function() {

	$('#dg').datagrid({
		/* fitColumns : true, */
		title : '<fmt:message key="account.title" />',
		autoRowHeight : false,
		striped : true,
		idField : 'id',
		method : 'GET',
		url : '/account/all',
		emptyMsg : '<fmt:message key="account.any.null" />',
		pagination : true,
		/* pagePosition: 'top', */
		rownumbers : true,
		/* singleSelect: true, */
		ctrlSelect : true,
		pageNumber : 1,
		pageSize : 5,
		pageList : [ 5, 10 ],
		sortName : 'id',
		sortOrder : 'desc',
		remoteSort : true,
		queryParams : {
			name : $('#ss').searchbox('getValue')
		},
		loadFilter : function(page) {
			//alert(JSON.stringify(page));
			return {
				'total' : page.totalElements,
				'rows' : page.content
			};
		},
		onLoadSuccess : function(data) {
			//alert('onLoadSuccess');
			//alert(JSON.stringify(account.data));
			if(account.data) {
				$('#dg').datagrid('clearSelections').datagrid('selectRecord', account.data.id);
				account.data = null;
			}
		},
		columns : [ [ 
			{ field : 'checkbox', checkbox : true }, 
			{ field : 'id', title : 'ID', align:'right', sortable : true, order : 'desc' }, 
			{ field : 'name', title : 'Name', width : 120 }, 
			{ field : 'version', title : 'Version', hidden : true} ] ],
		toolbar :  '#tb' /*[ {
			text : 'Add',
			iconCls : 'icon-add',
			handler : function() {
				$('#dl').dialog('open').dialog('setTitle','New Account').dialog('refresh', 'form');
			}
		}, {
			text : 'Remove',
			iconCls : 'icon-remove',
			handler : function() {
				var rows = $('#dg').datagrid('getSelections');
				alert(JSON.stringify(rows));
				alert(rows.length);
				
				if(rows.length > 0) {
					$.messager.confirm('Confirm','Are you sure you want to REMOVE this account?',function(r){
						if(r) {
							$.ajax({
								url : '/account',
								method : 'DELETE',
								contentType : 'application/json;charset=UTF-8',
								data : JSON.stringify(rows),
								dataType : 'json',
								headers : headers,
								success : function(result, message, status) {
									$('#dg').datagrid('reload').datagrid('unselectAll');
								}
							});
						}
					});
				}				
			}
		}, {
			text : 'Edit',
			iconCls : 'icon-edit',
			handler : function() {
				var row = $('#dg').datagrid('getSelected');
				alert(JSON.stringify(row));
				if(row != null) {
					$('#dl').dialog('open').dialog('setTitle','Edit Account').dialog('refresh', '' + row.id);
				}
			}
		}, '-', {
			text : 'Save',
			iconCls : 'icon-save',
			handler : function() {
				alert('Save');
			}
		}]*/
	});
	
	$('#dl').dialog({
		title: 'Account',
		width: 300,
		/* height: 180, */
		closed: true,
		cache: true,
		href: 'form',
		modal: true,
		onLoad: function(){
			
		},
		buttons: [{
			text:'Save',
			iconCls:'icon-save',
			handler:function(){
				
				if($('form#account').form('validate')) {
					var data = {
						'id' : $('input#id').val(),
						'name' : $('input#name').val(),
						'version' : $('input#version').val()
					};
						
					alert(JSON.stringify(data));
						
					$.ajax({
						url : '/account',
						method : 'PUT',
						contentType : 'application/json;charset=UTF-8',
						data : JSON.stringify(data),
						dataType : 'json',
						headers : headers,
						success : function(result, message, status) {
							account.data = result;
							alert(JSON.stringify(result));
							alert(JSON.stringify(message));
							alert(JSON.stringify(status));
							$('#dl').dialog('close');
							if(data.id) {
								$('#dg').datagrid('reload', {name : $('#ss').searchbox('getValue')});
							} else {
								$('#ss').searchbox('clear');
								alert('result_id:' + result.id);
								$('#dg').datagrid('load', {name : ''});
							}
						},
						error : function(result, message, status) {
							for(var i=0; i < result.responseJSON.length; i++) {
								alert(JSON.stringify(result.responseJSON[i].defaultMessage));
							}
						}
					});
				}
			}
		}]
	}).dialog('refresh', 'form');
	
});

var csrfHeader = $("meta[name='_csrf_header']").attr('content');
var csrfToken = $("meta[name='_csrf']").attr('content');
var headers = {};
headers[csrfHeader] = csrfToken;

var account = {
	data : null,
	add : function() {
		$('#dl').dialog('open').dialog('setTitle','New Account').dialog('refresh', 'form');
	},
	edit : function() {
		var row = $('#dg').datagrid('getSelected');
		alert(JSON.stringify(row));
		if(row) {
			$('#dl').dialog('open').dialog('setTitle','Edit Account').dialog('refresh', '' + row.id);
		}
	},
	remove : function() {
		var rows = $('#dg').datagrid('getSelections');
		alert(JSON.stringify(rows));
		
		if(rows.length > 0) {
			$.messager.confirm('Confirm','Are you sure you want to REMOVE this account?',function(r){
				if(r) {
					$.ajax({
						url : '/account',
						method : 'DELETE',
						contentType : 'application/json;charset=UTF-8',
						data : JSON.stringify(rows),
						dataType : 'json',
						headers : headers,
						success : function(result, message, status) {
							$('#dg').datagrid('clearSelections').datagrid('reload', {name : $('#ss').searchbox('getValue')});
						}
					});
				}
			});
		}	
	},
	tip : function() {
		$('#dg').datagrid('selectRecord', 9);
	},
	search : function(value) {
		alert(value);
		$('#dg').datagrid('load', {name : value});
	}
};
</script>
</head>
<body>
	<table id="dg" />
	<div id="dl" />
	<table id="tb" cellspacing="0" cellpadding="0">
		<tr>
			<td>
			<a href="javascript:account.add();" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'">Add</a>
			</td>
	    	<td>
	    	<a href="javascript:account.edit();" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-edit'">Edit</a>
	    	</td>
	    	<td>
	    	<a href="javascript:account.remove();" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-remove'">Remove</a>
	    	</td>
	    	<td>
	    	<a href="javascript:account.tip();" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-tip'">Tip</a>
	    	</td>
	    	<td>
	    	<div class="datagrid-btn-separator"></div>
	    	</td>
	    	<td>
	    	<input id="ss" class="easyui-searchbox" style="width:130px" data-options="prompt:'Please Input Name',searcher:account.search">
	    	</td>
	    	<td width="100%"></td>
	    </tr>
	</table>
</body>
</html>