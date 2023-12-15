function includeJs(file) { 
  
  var script  = document.createElement('script'); 
  script.src  = file; 
  script.type = 'text/javascript'; 
  script.defer = true; 
  
  document.getElementsByTagName('head').item(0).appendChild(script); 
  
} 
 
 function includeCSS(file) { 
  
  var css  = document.createElement('link'); 
  css.href  = file; 
  css.type = 'text/css'; 
  css.rel = 'stylesheet'; 
  
  document.getElementsByTagName('head').item(0).appendChild(css); 
} 
  
/* Include Many js files */
includeJs('/webjars/jquery/3.5.1/jquery.min.js'); 
includeJs('/js/jquery-ui.min.js'); 
includeJs('/webjars/bootstrap/4.5.2/js/bootstrap.bundle.min.js');
includeJs('/js/bootstrap-datepicker.js');
includeJs('/js/summernote-lite.js');
includeJs('/js/main.js');

/* Include Many CSS files */
includeCSS('/css/jquery-ui.min.css');
includeCSS('/webjars/bootstrap/4.5.2/css/bootstrap.min.css'); 
includeCSS('/css/datepicker.css');
includeCSS('/css/summernote-lite.css')
includeCSS('/css/main.css');
