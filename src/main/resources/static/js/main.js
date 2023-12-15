
$('#downloadexcel').css('background-color','#cde12b2e');
$('#downloadexcel').css('border-color','#ff5730');

var today = new Date();
var dd = today.getDate();
var mm = today.getMonth() + 1; //January is 0!
var yyyy = today.getFullYear();
if (dd < 10) {
	dd = '0' + dd
}
if (mm < 10) {
	mm = '0' + mm
}
$(document).ready(function() {
	$('[data-toggle="tooltip"]').tooltip();
});

function confirmation() {
	return confirm('Do you want to proceed?');
}


function loadingOut(loading) {
	setTimeout(() => loading.out(), 200);
	//				loading.out();
}

today = yyyy + '-' + mm + '-' + dd;
/*var closedate = document.getElementById("closedate");
var birthdate = document.getElementById("dateOfBirth");
if(birthdate != null) {
	birthdate.setAttribute("max", today);
}
if(closedate != null) {
	closedate.setAttribute("max", today);
}*/
function generate() {
	var noticeId = $("#noticeId").val();
	console.log(noticeId);
	var xhr = new XMLHttpRequest();
	xhr.open('GET', '/pdf/' + noticeId, true);
	xhr.responseType = 'arraybuffer';
	xhr.onload = function(e) {
		if (this.status == 200) {
			var blob = new Blob([this.response], { type: "application/pdf" });
			var link = document.createElement('a');
			link.href = window.URL.createObjectURL(blob);
			document.getElementById("embedPDF").data = link.href;
		}
	};
	xhr.send();
}



/*function FileTypeValidate() {
	
	
	var noticeId = ($('#noticeId').val());
	
	
	 console.log('hello');
	var fileUpload = $('#FileUpload1').val();
    
	var totalvacancy = parseInt($('#totalvacancy').val());
	var casteTotal = 0;
    
	var ss = $('#castvacancy0').val();
    
	for(var i=0;i<noticeObj.category.length;i++) {
//	    	if(noticeObj.category[i].vacancy != 0) {
				var name = '#castvacancy'+i;
			casteTotal += parseInt($(name).val());
//	    	}
	}
	
	var extension = fileUpload.substring(fileUpload.lastIndexOf('.'));
	var ValidFileType = ".pdf";
	var fileTypeCheck = true;
	var vacancycheck = false;
	
	if(totalvacancy == (casteTotal)) {
		vacancycheck = true;
	} else {
		alert("Please check total number of vacancy should combination of sc,st,obc and general");
	}



	if (fileUpload.length > 0) {
	
		if (ValidFileType.toLowerCase().indexOf(extension) < 0) {
			alert("please select valid file type...(PDF)");
		}
		else {
			fileTypeCheck = true;
		}
	} 

	console.log(vacancycheck+""+fileTypeCheck);
	if(vacancycheck && fileTypeCheck) {
		return true;
	}
	return false; 
}*/

function FileTypeValidateExcel(sender) {

	console.log('helloExcel');
	var fileUpload = sender.value;

	var extension = fileUpload.substring(fileUpload.lastIndexOf('.'));
	var ValidFileType = new Array(".csv", ".xls", ".xlsx", ".ods");
	var fileTypeCheck = false;


	if (fileUpload.length > 0) {

		if (ValidFileType.indexOf(extension) < 0) {
			alert("please select valid file type...(Excel)");
		}
		else {
			fileTypeCheck = true;
		}
	}
	else {
		alert("please select file for upload...");
	}
	if (fileTypeCheck) {
		$('#uploadMPSC').prop('disabled', false);
		return true;
	}
	$('#uploadMPSC').prop('disabled', true);
	return false;
}

$(function() {
	$('#language_selection').change(function() {
		var selections = $("#language_selection :selected");
		var html = '';
		$.each(selections, function(i, item) {
			html += $(item).text() + ':<input type="file" name="' + $(item).val() + '" accept=".pdf" id="' + $(item).val() + '" /><br>';
		})
		$('#divFileInput').html(html);
	})

})


function downloadCandiDoc(id, fileName) {

	var xhr = new XMLHttpRequest();
	xhr.open('GET', '/downloadDoc/' + id + '/' + fileName, true);
	xhr.responseType = 'arraybuffer';
	xhr.onload = function(e) {
		if (this.status == 200) {
			var blob = new Blob([this.response], { type: "application/pdf" });
			var link = document.createElement('a');
			link.href = window.URL.createObjectURL(blob);
			link.download = fileName + ".pdf";
			link.click();
		}
	};
	xhr.send();
}

function sendReminder(id, fileName) {

	var xhr = new XMLHttpRequest();
	xhr.open('GET', '/sendReminder/' + id + '/' + fileName, true);
	xhr.responseType = 'arraybuffer';
	xhr.onload = function(e) {
		if (this.status == 200) {

		}
	};
	xhr.send();
}


function changeValue(casteCode, index) {
	console.log('change event');
	var ind = 0;
	for (var i = 0; i < noticeObj.category.length; i++) {
		if (noticeObj.category[i].category_Code == casteCode) {
			ind = i;
			break;
		}
	}
	var currentvalue = parseInt($('#castvacancy' + index).val());
	//		var oldvalue = parseInt($('#castvacancy'+ind).val());
	var oldvalue = parseInt(noticeObj.category[ind].vacancy);
	if (oldvalue > currentvalue || oldvalue == currentvalue) {
		document.getElementById('castvacancy' + ind).value = oldvalue - currentvalue;
	}
}

function candidateValid() {
	var id = $('#docId').val();
	if (id != '0' && id.length > 0) {
		return true;
	} else {
		alert('Please Input Doc Id');
		$('#docId').focus();
	}
	return false;
}
$(document).ready(function() {



	$('#editor').summernote({
		height: 200,                 // set editor height
		minHeight: null,             // set minimum height of editor
		maxHeight: null,             // set maximum height of editor
		focus: false,                  // set focus to editable area after initializing summernote
		toolbar: [
			['style', ['style']],
			['font', ['bold', 'underline', 'clear']],
			['fontname', ['fontname']],
			['color', ['color']],
			['para', ['ul', 'ol', 'paragraph']],
			['table', ['table']]
		],
		tabDisable: false,
		codeviewFilter: false,
		codeviewIframeFilter: true
	});

	$('#editorletter').summernote({
		height: 200,                 // set editor height
		minHeight: null,             // set minimum height of editor
		maxHeight: null,             // set maximum height of editor
		focus: false,                  // set focus to editable area after initializing summernote
		toolbar: [
			['style', ['style']],
			['font', ['bold', 'underline', 'clear']],
			['fontname', ['fontname']],
			['color', ['color']],
			['para', ['ul', 'ol', 'paragraph']],
			['table', ['table']]
		],
		tabDisable: false,
		codeviewFilter: false,
		codeviewIframeFilter: true
	});


	$('#editorlettersmall').summernote({
		height: 400,                 // set editor height
		minHeight: null,             // set minimum height of editor
		maxHeight: null,             // set maximum height of editor
		focus: false,                  // set focus to editable area after initializing summernote
		toolbar: [
			['style', ['style']],
			['font', ['bold', 'underline', 'clear']],
			['fontname', ['fontname']],
			['color', ['color']],
			['para', ['ul', 'ol', 'paragraph']],
			['table', ['table']]
		],
		tabDisable: false,
		codeviewFilter: false,
		codeviewIframeFilter: true
	});


	$('#testingxyz').delay(10000).fadeOut(10000);



});

function getPDFFF() {

	var xhr = new XMLHttpRequest();
	xhr.open('GET', '/getPDFFF', true);
	xhr.responseType = 'arraybuffer';
	xhr.onload = function(e) {
		if (this.status == 200) {
			var blob = new Blob([this.response], { type: "application/pdf" });
			var link = document.createElement('a');
			link.href = window.URL.createObjectURL(blob);
			link.download = "xyz.pdf";
			link.click();
		}
	};
	xhr.send();
}
$('form').attr('autocomplete', 'off');

function FileTypeValidatePDF(sender) {

	var fileUpload = sender.value;

	var extension = fileUpload.substring(fileUpload.lastIndexOf('.'));
	var ValidFileType = new Array(".pdf");
	var fileTypeCheck = false;


	if (fileUpload.length > 0) {

		if (ValidFileType.indexOf(extension) < 0) {
			alert("please select valid file type...(PDF)");
		}
		
		else {
			fileTypeCheck = true;
		}
	}
	else {
		alert("please select file for upload...");
	}
	if (fileTypeCheck) {
		return true;
	}
	return false;
}


function abc() {
	var formData = JSON.stringify({ 'id': 10 });
	$.ajax({
		url: "/location",
		type: "post",
		data: formData,
		success: function(d) {
			alert(d);
		}
	});
}
$(function() {
	$('.nav a').click(function() {
		$(this).parent().addClass('active').siblings().removeClass('active')
	})
})

$(document).ready(function() {
	$(function() {
		if (!$("#link-listss").is(":blk-transpose"))
			$("#link-listss").transpose({ mode: 0 });

		if (!$("#link-listss").is(":blk-transpose"))
			$("#link-listss").transpose({ mode: 1 });

	});
})

$(document).ready(function() {
	$('.confirmModal').click(function(e) {
		var confirmx = confirm("Are you sure want to proceed ahead?");
		if (confirmx) {
			return true;
		}
		return false;
	});
});



$(document).ready(function() {

		function getWordCount(wordString) {
		  var words = wordString.split(" ");
		  words = words.filter(function(words) { 
		    return words.length > 0
		  }).length;
		  return words;
		}
		
		//add the custom validation method
		jQuery.validator.addMethod("wordCount",
		   function(value, element, params) {
		      var count = getWordCount(value);
		      if(count <= params[0]) {
		         return true;
		      }
		   },
		   jQuery.validator.format("Maximum of {0} words is required here.")
		);

	$.validator.addMethod('filesize', function(value, element, param) {
		return this.optional(element) || (element.files[0].size <= param)
	}, 'File size must be less than {0} bytes');

	$('#noticeregisteridmpsc').validate({
		rules: {

			fileName: {
				required: false,
				accept: "application/pdf",
				filesize: 5242880,
			},
			file: {
				required: false,
				accept: "application/pdf",
				filesize: 5242880,
			},
			filedata: {
				required: false,
				accept: "application/pdf",
				filesize: 5242880,
			},
			finalcourtorder: {
				required: false,
				accept: "application/pdf",
				filesize: 5242880,
			},
			revisedfileName: {
				required: false,
				accept: "application/pdf",
				filesize: 5242880,
			},
			uploadOder: {
				required: false,
				accept: "application/pdf",
				filesize: 5242880,
			},
			uploadOrderofAppointment: {
				required: false,
				accept: "application/pdf",
				filesize: 5242880,
			},
			inquiryReportfileUpload: {
				required: false,
				accept: "application/pdf",
				filesize: 5242880,
			},
			UploadOrderReInstatment: {
				required: false,
				accept: "application/pdf",
				filesize: 5242880,
			},
			uploadOrderActualSuspensionName: {
				required: false,
				accept: "application/pdf",
				filesize: 5242880,
			},
			fileNameTransferApproval: {
				required: false,
				accept: "application/pdf",
				filesize: 5242880,
			},
			uploadOrderDeemedSuspension: {
				required: false,
				accept: "application/pdf",
				filesize: 5242880,
			},
			extensionUploadOrder: {
				required: false,
				accept: "application/pdf",
				filesize: 5242880,
			},
			fileSubject: {
		      required: true,
		      maxlength: 1000,
		      minlength: 10
		    },
		    caseSummary: {
		      required: true,
		      wordCount: [150]
		    },
		    totalNoOfEmployee: {
		      required: true,
		      maxlength: 99,
		      minlength: 1
		    }  
		       
		},
		messages:
		{
			file: "File must be PDF, less than 5MB, File Name character can not be greater than 5 character"
		},
		
	   //var cnfMsg = [[${{confirmMsg}}]];
		
		submitHandler: function(form) {
			var aa = $(document.activeElement).val();
			$("#inputaction").val(aa);
			swal({
				title: "Do you want to save?",
				text: "",
				
				buttons: true,
				dangerMode: true,
				buttons: ["No", "Yes"]
			}).then(function(isConfirm) {
				if (isConfirm) {
					const submitFormFunction = Object.getPrototypeOf(form).submit;
					submitFormFunction.call(form);
					var loading = new Loading();
				}
			});
		}
	});




	

	



});


$(document).ready(function () {
	 var cookies = document.cookie.split(';');
//var language = document.cookie["language"];
	console.log(cookies);
	
	
$('#ui-basicreports ol li a').click(function() {
    localStorage.setItem('collapseItem', $(this).attr('id'));
    localStorage.setItem('listItem','#ui-basicreports');
});

$('#ui-basicreports ol li a').click(function() {
    localStorage.setItem('collapseItem', $(this).attr('id'));
    localStorage.setItem('listItem','#ui-basicreports');
});

$('#ui-basiconlineio ul li a').click(function() {
    localStorage.setItem('collapseItem', $(this).attr('id'));
    localStorage.setItem('listItem','#ui-basiconlineio');
});

$('#ui-basic10 ul li a').click(function() {
    localStorage.setItem('collapseItem', $(this).attr('id'));
    localStorage.setItem('listItem','#ui-basic10');
});


$('#rulesorders').click(function() {
	 localStorage.setItem('collapseItem', $(this).attr('id'));
    localStorage.setItem('listItem','');
});

$('#uploadorders').click(function() {
	 localStorage.setItem('collapseItem', $(this).attr('id'));
    localStorage.setItem('listItem','');
});

$('#processdecerticate').click(function() {
	 localStorage.setItem('collapseItem', $(this).attr('id'));
    localStorage.setItem('listItem','');
});

$('#editcases').click(function() {
	 localStorage.setItem('collapseItem', $(this).attr('id'));
    localStorage.setItem('listItem','');
});

$('#gettemplates').click(function() {
	 localStorage.setItem('collapseItem', $(this).attr('id'));
    localStorage.setItem('listItem','');
});

$('#getcontractinqlist').click(function() {
	 localStorage.setItem('collapseItem', $(this).attr('id'));
    localStorage.setItem('listItem','');
});

$('#getdivinqlist').click(function() {
	 localStorage.setItem('collapseItem', $(this).attr('id'));
    localStorage.setItem('listItem','');
});


$('#getioreq').click(function() {
	 localStorage.setItem('collapseItem', $(this).attr('id'));
    localStorage.setItem('listItem','');
});

$('#getiocases').click(function() {
	 localStorage.setItem('collapseItem', $(this).attr('id'));
    localStorage.setItem('listItem','');
});

$('#assigniocases').click(function() {
	 localStorage.setItem('collapseItem', $(this).attr('id'));
    localStorage.setItem('listItem','');
});


$('#getiocasesreportsub').click(function() {
	 localStorage.setItem('collapseItem', $(this).attr('id'));
    localStorage.setItem('listItem','');
});

$('#getiocasespendingreport').click(function() {
	 localStorage.setItem('collapseItem', $(this).attr('id'));
    localStorage.setItem('listItem','');
});


$('#getioupcominghearing').click(function() {
	 localStorage.setItem('collapseItem', $(this).attr('id'));
    localStorage.setItem('listItem','');
});

var collapseItem = localStorage.getItem('collapseItem'); 
var listItem = localStorage.getItem('listItem');
if (collapseItem || listItem) {
   $(listItem).addClass('show');
   var item = '#'+collapseItem;
   if(item == '#alldepartgroup' || item == '#departwisegroup') {
	 $('#ui-basicgroup').addClass('show');
	}
	if(item=='#suspensionpendingcases' || item == '#departmentsuspensionpendingcases' || item == '#withoutdesuspensionpendingcases') {
		$('#ui-basicsus').addClass('show');
	}
	if(item=='#suswithoutde' || item == '#reinstwithoutde' || item == '#prosecwithoutde') {
		$('#ui-basic3').addClass('show');
	}
	if(item=='#getdivinqlist' || item == '#getcontractinqlist') {
		$('#ui-basiconlineio').addClass('show');
	}
	
   $(item).css('background-color','black');
//   $("li.sonItem").css("background-color","black");
}

$('#homepage').click(function() {
	localStorage.setItem('collapseItem', '');
    localStorage.setItem('listItem','');
    });
    
 $('#homepagenavbar').click(function() {
	localStorage.setItem('collapseItem', '');
    localStorage.setItem('listItem','');
    });  
    
     $('#logoutstorage').click(function() {
	localStorage.setItem('collapseItem', '');
    localStorage.setItem('listItem','');
    });  

});


