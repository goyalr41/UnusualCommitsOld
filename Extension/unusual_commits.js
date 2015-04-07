jQuery(function($){
  $("a.sha.btn.btn-outline").each(function(){
	var value = $(this).html();
	console.log(value);
	$(this).css('background-color','#fcc');
	sendAjax(value);
  });
});

function sendAjax(value) {
 
    // get inputs
    var article = new Object();
    article.title = value;
 
    $.ajax({
        url: "https://128.237.251.212:8443/com.hmkcode/abcd",
        type: 'POST',
        dataType: 'json',
		crossDomain: true,
        data: JSON.stringify(article),
        contentType: 'application/json',
        mimeType: 'application/json',
 
        success: function (data) {
            $("tr:has(td)").remove();
 
            $.each(data, function (index, article) {
 
                var td_categories = $("<td/>");
                $.each(article.categories, function (i, tag) {
                    var span = $("<span class='label label-info' style='margin:4px;padding:4px' />");
                    span.text(tag);
                    td_categories.append(span);
                });
 
                var td_tags = $("<td/>");
                $.each(article.tags, function (i, tag) {
                    var span = $("<span class='label' style='margin:4px;padding:4px' />");
                    span.text(tag);
                    td_tags.append(span);
                });
 
                $("#added-articles").append($('<tr/>')
                        .append($('<td/>').html("<a href='"+article.url+"'>"+article.title+"</a>"))
                        .append(td_categories)
                        .append(td_tags)
                );
 
            }); 
        },
        error:function(data,status,er) {
            alert("error: "+data+" status: "+status+" er:"+er);
        }
    });
}

//var getStyleofbody = document.getElementsByClassName("sha btn btn-outline")[0].html();
//console.log(getStyleofbody);