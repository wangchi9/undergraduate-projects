var serviceURL = "https://mathlab.utsc.utoronto.ca/courses/cscc09f12/wangchi9/c01/"

function loadXMLDoc(dname)
{
if (window.XMLHttpRequest)
  {
  xhttp=new XMLHttpRequest();
  }
else
  {
  xhttp=new ActiveXObject("Microsoft.XMLHTTP");
  }
xhttp.open("GET",dname,false);
xhttp.send();
return xhttp.responseXML;
} 

function validate(){
	if(('admin'== document.getElementById('input_name').value)&&('admin'== document.getElementById('input_pass').value)){
		location.href="admin.html";		
		return true;
	}else{
		xmlDoc=loadXMLDoc("accounts.xml");
		var users=xmlDoc.documentElement;
		if(document.getElementById('input_name').value[0]=='t'){//log in as tutor
			var nodes=users.getElementsByTagName('tutor');
		}else{
			var nodes=users.getElementsByTagName('student');
		}
		for(i=0;i<nodes.length;i++){
			if(document.getElementById('input_name').value==nodes[i].getElementsByTagName('id')[0].childNodes[0].nodeValue){
				if(document.getElementById('input_pass').value==nodes[i].getElementsByTagName('password')[0].childNodes[0].nodeValue){
					location.href="#MyCourses";
					document.getElementById('loginpg').user=document.getElementById('input_name').value;
					document.getElementById('loginpg').user_name=nodes[i].getElementsByTagName('name')[0].childNodes[0].nodeValue;
					document.getElementById('loginpg').user_email=nodes[i].getElementsByTagName('email')[0].childNodes[0].nodeValue;
					document.getElementById('loginpg').courses=nodes[i].getElementsByTagName('courses')[0].childNodes[0].nodeValue;
					document.getElementById('mycourseheader').innerHTML=' MY COURSES FOR '+nodes[i].getElementsByTagName('name')[0].childNodes[0].nodeValue;
					return true;
				}else{
					document.getElementById('err').innerHTML='Invalid Account name or Password';
					return false;
				};
			}
		}
	}
	document.getElementById('err').innerHTML='Invalid Account name or Password';
	return false;
}


function inCourselist(course, courselist) {
	for(var i=0;i<courselist.length;i++){
		if(courselist[i]==course){
			return true;
		}
	}
	return false;	
}


function getCourses() {
	/* Make a server-side CGI-Saxon-XSLT call to retrieve data from a
       server-side routes represented as XML data.  The
       XSLT script returns a JSON dictionary representation of the XML
       routes data.*/

	$.getJSON(serviceURL + "course.cgi", function(data) {
	    var courses = data.items; 
	    $.each(courses, function(index, course) {				
			var courseItem = '<li><a href="#Course" id="' + course.id  
		    + '" title="' + course.display_name + '" info="' + course.info + '" time="'+ course.time + '" data-role="button"' 
		    + ' data-inline="true" data-mini="true" data-icon="arrow-r"' 
		    + ' data-theme="b" class="btn getcourse" data-transition="flip">' 
		    + course.id + '    ' + course.display_name + '</a></li>';
		$('#CoursesList').append(courseItem);}); 
	$('#CoursesList').listview('refresh');});
};

$('#Courses').live("pageinit", function(event) {
	/* When the page with id Routes is initialized,
	   invoke the getRoutes() function 
	*/
	getCourses();
});
/*
$('#MyCourses').live("pageinit", function(event) {
	/* When the page with id Routes is initialized,
	   invoke the getRoutes() function 

	$('#MyCourseList').empty();
	var n=document.getElementById('loginpg').courses.split(" ");
	$.getJSON(serviceURL + "course.cgi", function(data) {
	    var courses = data.items; 
	    $.each(courses, function(index, course) {
			for(var i=0;i<n.length;i++){
			if (course.id==n[i]){
			var courseItem = '<li><div data-type="horizontal" class="ui-btn-right"><a href="#Course" id="' + course.id  
		    + '" title="' + course.display_name + '" info="' + course.info + '" time="'+ course.time + '" data-role="button"' 
		    + ' data-inline="true" data-mini="true" data-icon="arrow-r"' 
		    + ' data-theme="b" class="btn getcourse" data-transition="flip">' 
		    + course.id + '    ' + course.display_name + '</a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#MyCourses" class="btn dropcourse" courseid="'
		    +  course.id + '" data-role="button" data-icon="star" data-theme="b">DROP COURSE<a></div></li>';
		$('#MyCourseList').append(courseItem);}}
});
	$('#MyCourseList').listview('refresh');});
});
*/
$('#MyCourses').live("pageshow", function(event) {
	/* When the page with id Routes is initialized,
	   invoke the getRoutes() function */

	$('#MyCourseList').empty();
	var n=document.getElementById('loginpg').courses.split(" ");
	$.getJSON(serviceURL + "course.cgi", function(data) {
	    var courses = data.items; 
	    $.each(courses, function(index, course) {
			for(var i=0;i<n.length;i++){
			if (course.id==n[i]){
			var courseItem = '<li><div data-type="horizontal" class="ui-btn-right"><a href="#Course" id="' + course.id  
		    + '" title="' + course.display_name + '" info="' + course.info + '" time="'+ course.time + '" data-role="button"' 
		    + ' data-inline="true" data-mini="true" data-icon="arrow-r"' 
		    + ' data-theme="b" class="btn getcourse" data-transition="flip">' 
		    + course.id + '    ' + course.display_name + '</a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a id="droping" href="#" class="btn dropcourse" courseid="'
		    +  course.id + '" data-role="button" data-icon="star" data-theme="b">DROP COURSE<a></div></li>';
		$('#MyCourseList').append(courseItem);}}
});
	$('#MyCourseList').listview('refresh');});
	$('.dropcourse').button();
});




$('#TimeTable').live("pageinit", function(event) {
	/* When the page with id Routes is initialized,
	   invoke the getRoutes() function */
	var n=document.getElementById('loginpg').courses.split(" ");
	$.getJSON(serviceURL + "course.cgi", function(data) {
	    var courses = data.items; 
	    $.each(courses, function(index, course) {
			for(var i=0;i<n.length;i++){
			if (course.id==n[i]){
			document.getElementById(course.time).innerHTML=course.id +"  "+ course.display_name;			
}}
});
});
});

$('#TimeTable').live("pageshow", function(event) {
	/* When the page with id Routes is initialized,
	   invoke the getRoutes() function */
	var n=document.getElementById('loginpg').courses.split(" ");
	$.getJSON(serviceURL + "course.cgi", function(data) {
	    var courses = data.items; 
	    $.each(courses, function(index, course) {
			for(var i=0;i<n.length;i++){
			if (course.id==n[i]){
			document.getElementById(course.time).innerHTML=course.id +"  "+ course.display_name;			
}}
});
});
});


$('a.getcourse').live("click", function(event) {
	document.getElementById('enrollerr').innerHTML="";
	var courseid = $(this).attr('id');  // get the courseid from the event link
	var courseName = $(this).attr('title');
	var courseINFO = $(this).attr('info');
	var courseTIME = $(this).attr('time');

	document.getElementById('enroll').courseid=courseid;
	$('#CourseInfoList').empty();
	var courseINFOItem='<li><span>'+courseid+'</span></li><li><span>'+courseName+'</span></li><li><span>'+courseINFO+'</span></li><li><span>'+courseTIME+'</span></li>';
	$('#CourseInfoList').append(courseINFOItem);
	
});

$('a.enrolling').live("click", function(event) {
	var courseid = document.getElementById('enroll').courseid; 
	var n=document.getElementById('loginpg').courses.split(" ");
	if(inCourselist(courseid,n)){
		document.getElementById('enrollerr').innerHTML='The course is already in your course list!';
	}else{
	xmlDoc=loadXMLDoc("accounts.xml");
	document.getElementById('loginpg').courses += " " + courseid;
	var users=xmlDoc.documentElement;
	if(document.getElementById('loginpg').user[0]=='t'){//log in as tutor
			var nodes=users.getElementsByTagName('tutor');
		}else{
			var nodes=users.getElementsByTagName('student');
		}
	for(i=0;i<nodes.length;i++){
		if(document.getElementById('loginpg').user==nodes[i].getElementsByTagName('id')[0].childNodes[0].nodeValue){
			nodes[i].getElementsByTagName('courses')[0].childNodes[0].nodeValue=document.getElementById('loginpg').courses;
			//document.getElementById('enrollerr').innerHTML=nodes[i].getElementsByTagName('courses')[0].childNodes[0].nodeValue;
		}
	}
	location.href="#MyCourses";
	}
});


$('a.dropcourse').live("click", function(event) {
	var courseid = $(this).attr('courseid');//document.getElementById('droping').courseid; 
	var n=document.getElementById('loginpg').courses.split(" ");
	var courseid="";
	document.getElementById('enrollerr').innerHTML=courseid;
	if(n[0]!=courseid){
		courselist=n[0];
	}
	if(n.length>0){
		for(var i=1;i<n.length;i++){
			if (courseid!=n[i]){
				courselist += " "+n[i];
			}
		}
	}
	document.getElementById('enrollerr').innerHTML += "aaa"+courselist;
	document.getElementById('loginpg').courses=courselist;
	$(this).closest('li.ui-li').remove();
	$('#MyCourseList').listview('refresh');
	$.mobile.changePage("#MyCourses");
});