var serviceURL = "https://mathlab.utsc.utoronto.ca/courses/cscc09f12/wangchi9/c01/"

function getCourses() {
	/* Make a server-side CGI-Saxon-XSLT call to retrieve data from a
       server-side routes represented as XML data.  The
       XSLT script returns a JSON dictionary representation of the XML
       users data.*/

	$.getJSON(serviceURL + "course.cgi", function(data) {
	    var courses = data.items; 
	    $.each(courses, function(index, course) {				
 			var courseItem = '<li><span>' + course.id + '   ' + course.display_name + '<br /><a href="" course_id="' + course.id  
		+ '" display_name="' + course.display_name + '" data-role="button"' 
		+ ' data-inline="true" data-mini="true" data-icon="star"'
		+ ' data-theme="b" class="btn editCourse" data-transition="flip">Edit Course</a>'
		+ '&nbsp;&nbsp;<a href="" course_id="' + course.id + '" data-role="button"'
		+ ' data-inline="true" data-mini="true" data-icon="arrow-r"'
		+ ' data-theme="b" class="btn deleteCourse" data-transition="flip">Delete Course</a></span></li>';
	    $('#CoursesList').append(courseItem);}); 
	$('#CoursesList').listview('refresh');});
};

/*
function getUsers() {
	/* Make a server-side CGI-Saxon-XSLT call to retrieve data from a
       server-side routes represented as XML data.  The
       XSLT script returns a JSON dictionary representation of the XML
       routes data.

};
*/

$('#Courses').live("pageinit", function(event) {
	getCourses();
});
