jQuery(function($){
  $("a.sha.btn.btn-outline").each(function(){
	var value = $(this).html();
	console.log(value);
	$(this).css('background-color','#fcc');
  });
});

var ws = new WebSocket("wss://127.0.0.1:8080/");

ws.onopen = function() {
    alert("Opened!");
    ws.send("Hello Server");
};

ws.onmessage = function (evt) {
    alert("Message: " + evt.data);
};

ws.onclose = function() {
    alert("Closed!");
};

ws.onerror = function(err) {
    alert("Error: " + err);
};

//var getStyleofbody = document.getElementsByClassName("sha btn btn-outline")[0].html();
//console.log(getStyleofbody);