	          var pushbar = new Pushbar({
	      		blur:true,
	      		overlay:true,
	      	});
	          $("#txtroute").text("ROUTE #: " + sessionStorage.getItem("RouteCode")); 
	          window.setInterval(function(){
	       		var randomColor = '#'+ ('000000' + Math.floor(Math.random()*16777215).toString(16)).slice(-6);
	       		$('#txtroute').css({
	         			'color' : randomColor,
	       		});
	     			}, 2000);