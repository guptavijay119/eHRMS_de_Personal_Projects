var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");

function getdata(urlstring,callback) {
    var loading = new Loading();
    $.ajax({
        url : urlstring,
        type : "GET",
        processData: false, //prevent jQuery from automatically transforming the data into a query string
        contentType: false,
        beforeSend: function(xhr) {
            // here it is
            xhr.setRequestHeader(header, token);
        },
        success: function(data) {
//            return data;
            loadingOut(loading);
            callback(data);
        },
        error: function() {
            loadingOut(loading);
            alert("Something went wrong")
        }
        
    });
    
}


function postdata(urlstring,body,callback) {
    var loading = new Loading();
    $.ajax({
        url: urlstring,
        type: "POST",
         enctype: 'multipart/form-data',
        data: body,
        processData: false, //prevent jQuery from automatically transforming the data into a query string
        contentType: false,
        beforeSend: function(xhr) {
            // here it is
            xhr.setRequestHeader(header, token);
        },
        success: function(d) {
//            return d;
            loadingOut(loading);
            callback(d);
        },
        error: function() {
            loadingOut(loading);
            alert("Something went wrong")
        }
    });
}

function postdatajson(urlstring,body,callback) {
    var loading = new Loading();
    $.ajax({
        url: urlstring,
        type: "POST",
        data : JSON.stringify(body),
//		dataType : 'json',
		contentType : "application/json",
        beforeSend: function(xhr) {
            // here it is
            xhr.setRequestHeader(header, token);
        },
        success: function(d) {
//            return d;
            loadingOut(loading);
            callback(d);
        },
        error: function() {
            loadingOut(loading);
            alert("Something went wrong")
        }
    });
}


function getdatafragments(urlstring,callback) {
    $.ajax({
        url : urlstring,
        type : "GET",
        processData: false, //prevent jQuery from automatically transforming the data into a query string
        contentType: false,
        beforeSend: function(xhr) {
            // here it is
            xhr.setRequestHeader(header, token);
        },
        success: function(data) {
//            return data;
            callback(data);
        },
        error: function() {
            alert("Something went wrong")
        }
        
    });
    
}
