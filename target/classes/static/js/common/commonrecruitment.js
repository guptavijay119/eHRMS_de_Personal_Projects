

var originalfilled = parseInt($('#filledPostByRecruitmentRules').val());

var d = new Date();
    var year = d.getFullYear();
    var month = d.getMonth();
    var day = d.getDate();
    var c = new Date(year + 1, 11, 31);
    $('#expecteddateprop').text('('+moment(c).format('DD-MMM-YYYY')+')');

		$(function () {

				setTimeout(function () {

					$("#link-listss").transpose({mode: 0});

					$("#link-listss").transpose("transpose");
					
					$("#modaltablebig").transpose({mode: 0});

					$("#modaltablebig").transpose("transpose");

				}, 100);

			});


$('#downloadList').click(function() {
	var ff = new FormData();
	ff.append('html1',$('#printdata').html());
//	ff.append('html2',$('#link-list').html()); 
	postdata('/probation/downloadorder/',ff,function(data) {
		jQuery.noConflict();
		$('#embedPDF').attr('data',data);
		$('#myModalFormDetails').modal('hide');
		$('#myModalprint').modal();
	});
});


			function removefileajax(idx) {
				var filenameid = $('#' + idx).text();
				if(confirm("Do you want to delete?")) {
					getdata('/recruitmentmpsc/removeproposalfile/' + idx + '/' + filenameid, function (data) {	
						location.reload();
					});
				}
			}

function changeactualvacancy() {
	var total = parseInt($('#sanctionedByRecruitmentRules').val());
	var filled = parseInt($('#filledPostByRecruitmentRules').val());
	var extra = parseInt($('#extravacancy').val());
	var expectedvacancy = parseInt($('#expectedvacancy').val());

	if (total > filled) {
		var actualvacancy = total - filled;
		$('#actualVacancy').val(total - filled);
		var totalvacancy = actualvacancy + extra + expectedvacancy;
		if (totalvacancy > total) {
			alert('Total Vacancy can not be greater than sanctioned strength');
			$('#filledPostByRecruitmentRules').val(originalfilled);
		} else {
			$('#totalvacancy').val(totalvacancy);
			calculatePercentage();
		}
	} else {
		alert('Filled post can not be greater than sanctioned strength');
		$('#filledPostByRecruitmentRules').val(originalfilled);
	}
}
var originaltotal = parseInt($('#totalvacancy').val());

/*$("input[type=radio][name=approved]").prop('checked', false);*/

function changeintotalvacancy() {
	var total = parseInt($('#sanctionedByRecruitmentRules').val());
	var actual = parseInt($('#actualVacancy').val());
	var extra = parseInt($('#extravacancy').val());
	if (extra < 0 || isNaN(extra)) {
		$('#extravacancy').val(0);
		extra = 0;
	}
	var expect = parseInt($('#expectedvacancy').val());
	if (expect < 0 || isNaN(expect)) {
		$('#expectedvacancy').val(0);
		expect = 0;
	}
	if (extra >= 0 && expect >= 0) {
		var totalvacancy = actual + extra + expect;
		if (totalvacancy > total) {
			alert('Total Vacancy can not be greater than sanctioned strength');
			$('#extravacancy').val(0);
			$('#expectedvacancy').val(0);
		} else {
			$('#totalvacancy').val(actual + extra + expect);
			calculatePercentage();
		}
	}

}

function calculatePercentage() {
	hidereservation1();
	var totalvacancy = parseInt($('#totalvacancy').val());
	var gr4 = totalvacancy * 0.04;
	var gr50 = totalvacancy * 0.5;
	$('#grVacancy4').val(parseInt(Math.ceil(gr4)));
	$('#grVacancy50').val(parseInt(Math.ceil(gr50)));

}

$("#btnTpHorizontal").click(function() {
	var currentMode = $("#link-listss").data("tp_mode");
	if (currentMode == undefined) {
		$("#link-listss").transpose("transpose");
		$("#btnTpHorizontal").html("Reset");
	}
	else {
		$("#link-listss").transpose("reset");
		$("#btnTpHorizontal").html("Transpose");
	}
});

function filledPostValidation(value,id, index,type) {
var santionedpost = parseInt($('#sanctionedByRecruitmentRules').val());
	var actualvacancyminimum = parseInt($('#actualvacancyminimum').val());
	var designationid = parseInt($('#designationidvalue').val());
	var filledPost = parseInt($('#filledPostByRecruitmentRules').val());
	var proposalid = $('#noticexId').val();
	if(proposalid.length>0) {
		
	} else {
	proposalid = null;
	}
		var ii = "vacancyDistributionList"+index+"."+type;
		var iu = "input[id='"+ii+"']";
//		console.log($(iu).val())
		var changedvalue = $(iu).val();
		var casteid = id;
		var type = type;
		var totaltype = "#"+type;
		$(totaltype).val(parseInt($(totaltype).val()) - parseInt(value) + parseInt(changedvalue));
/*		getdata("/recruitmentcptp/getReservationFragmentBig/" + santionedpost + "/" + actualvacancyminimum + "/" + designationid + '/' + filledPost + '/' + proposalid+'?casteid='+id+'&type='+type+'&changedvalue='+changedvalue, function(data) {
		$('#link-listss').replaceWith(data);
		setTimeout(function() {

			$("#link-listss").transpose({ mode: 0 });
			//        $("#link-listssx").transponse({ mode:0 });
			//	     }
			$("#link-listss").transpose("transpose");
			//	     $("#link-listssx").transpose("transpose");
			//		var xyz = $("#link-listssx").tableToJSON();
			//		console.log(xyz);
			$('#link-listss').show();

		}, 100);


		//		 var totalvacancy = $('#totalvacancy').val();

		$('#showreservation').hide();
		$('#hidereservation').show();

	});*/

	}

function showreservation1() {

	var santionedpost = parseInt($('#sanctionedByRecruitmentRules').val());
	var actualvacancyminimum = parseInt($('#actualvacancyminimum').val());
	var designationid = parseInt($('#designationidvalue').val());
	var filledPost = parseInt($('#filledPostByRecruitmentRules').val());
	var proposalid = $('#noticexId').val();
	if(proposalid.length>0) {
		
	} else {
	proposalid = null;
	}
	getdata("/recruitmentcptp/getReservationFragmentBig/" + santionedpost + "/" + actualvacancyminimum + "/" + designationid + '/' + filledPost + '/' + proposalid, function(data) {
		$('#link-listss').replaceWith(data);
		setTimeout(function() {

			$("#link-listss").transpose({ mode: 0 });
			//        $("#link-listssx").transponse({ mode:0 });
			//	     }
			$("#link-listss").transpose("transpose");
			//	     $("#link-listssx").transpose("transpose");
			//		var xyz = $("#link-listssx").tableToJSON();
			//		console.log(xyz);
			$('#link-listss').show();

		}, 100);


		//		 var totalvacancy = $('#totalvacancy').val();

		$('#showreservation').hide();
		$('#hidereservation').show();

	});
	
}

function hidereservation1() {
	$('#link-listss').hide();
	$('#showreservation').show();
	$('#hidereservation').hide();
}

function showreservationsmall() {
	var santionedpost = parseInt($('#totalSanctionedByDesignation').val());
	var totalvacancy = parseInt($('#totalvacancy').val());
	var designationid = parseInt($('#designationidvalue').val());

	getdata("/recruitmentcptp/getReservationFragmentSmall/" + santionedpost + "/" + totalvacancy + "/" + designationid, function(data) {
		$('#link-listssx').replaceWith(data);


		//		 var totalvacancy = $('#totalvacancy').val();

		$('#showreservation2').hide();
		$('#hidereservation2').show();

	});



	//		 $('#link-listssx').show();

}

function hidereservationsmall() {
	$('#link-listssx').hide();
	$('#showreservation2').show();
	$('#hidereservation2').hide();
}

$('#link-listss').hide();
$('#hidereservation').hide();
$('#link-listssx').hide();
$('#hidereservation2').hide();

