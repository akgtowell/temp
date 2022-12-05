

var xhr;

function getData(url, params) {

//alert(url);

    try {

        xhr = new XMLHttpRequest();       

        xhr.open('POST', url, false);

        xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
       
        xhr.onreadystatechange = function () {

            if (xhr.readyState == 4) {

                if (xhr.status == 200) {

                    postMessage(xhr.responseText);

                }
                else
                {
                    setInterval(function() { postMessage(xhr.responseText)}, 7000);

                 }   

            }

        };

        xhr.send(params);

    } catch (e) {

        postMessage('Error occured'+url);

    }

}

 


self.onmessage = function(event) { 
    var d=new Date();
    var n=d.toLocaleTimeString(); 
   var wsurl= event.data;
  
    
    for (var wsCounter=1; wsCounter < 10; wsCounter++) {
			var d=new Date();
			var n=d.toLocaleTimeString(); 
         
		getData(wsurl+"/table/" + wsCounter, "");
		
}
};



