function parseWebPage() {
	var parseAPI = "http://localhost:8088/api/parse";
	var url = document.getElementById("url").value;
	if (null == url || url.length == 0) {
		alert("Enter Valid URL");
		return;
	}

	$.ajax({
		url : parseAPI,
		type : "GET",
		data : "url=" + url, // https://www.spiegel.de/meinspiegel/login.html
		dataType : "json",
		beforeSend : function() {
			$('#tableData').empty();
			 $(".modal").show();
		},
		complete : function() {
			 $(".modal").hide();
		},
		success : function(response) {
			$(function() {
				$('#loader').hide();
				if (response.pageStatus == "INVALID_URL_FORMAT") {
					alert("Invalid URL Format!");
					return;
				}
				if (response.pageStatus == "UNREACHABLE_URL") {
					alert("Unreachable URL!");
					return;
				}

				if (response.pageStatus == "UNPARSABLE_HTML_CONTENT") {
					alert("Unparsable Page Content!");
					return;
				}

				var externalLinks;
				var internalLinks;
				var headings;

				var tableDataA = '<table>';
				tableDataA += '<tr><th>' + 'Page Title' + '</th><td>'+ response.title + '</td></tr>';
				tableDataA += '<tr><th>' + 'HTML Version' + '</th><td>'+ response.htmlVersion + '</td></tr>';
				tableDataA += '<tr><th>' + 'Requires Authentication'+ '</th><td>' + response.requiresAuthentication+ '</td></tr>';

				if (response.internalLinks.length > 0) {
					tableDataA += prepareLinksInfo('Internal Links',response.internalLinks);
				}

				if (response.externalLinks.length > 0) {
					tableDataA += prepareLinksInfo('External Links',response.externalLinks);
				}
				if (null != response.headings) {
					tableDataA += prepareHeadingsInfo('Headings',response.headings);
				}
				tableDataA += '</table>';
				$('#tableData').empty();
				$('#tableData').append(tableDataA);
			});
		}
	});
};

function prepareLinksInfo(rowTitle, links) {
	var row = '<tr><th>' + rowTitle + '</th>';
	row += '<td><table id="tableHeadings" style="width: 100%"><th> Link</th><th>Status</th><th nowrap>Validation Failure Reason</th>';
	$.each(links, function(i, item) {
		//row += '<tr><td>' + item.title + '</td>'
		row += '<tr><td>' + (item.isExternal==true?item.absoluteURL:item.url).substring(0,100) + '</td>';
		row += '<td>' + item.status + '</td>';
		row += '<td>' + (item.validationFailureReason==null?'--':item.validationFailureReason) + '</td></tr>';
	});
	row += "</table></td></tr>";
	return row;
};

function prepareHeadingsInfo(rowTitle, headings) {
	var row = '<th>' + rowTitle + '</th>';
	row += '<td><table id="tableHeadings" style="width: 100%">';
	for ( var key in headings) {
		row += '<tr><th rowspan=' + (headings[key].length + 1) + '>'+ key.toUpperCase() + '</th></tr>';
		for (var i = 0; i < headings[key].length; i++) {
			row += '<tr><td nowrap>' + (headings[key][i]) + '</td></tr>';
		}
	}
	row += "</table></td>";
	return row;
};
