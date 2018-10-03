function InvalidMsg(textbox) {

	var apass = document.getElementById('password').value;
	var apassnew = document.getElementById('repassword').value;
	if(apass != apassnew){
        textbox.setCustomValidity('Veuillez saisir le meme mot de passe !');
    }    
    else {
        textbox.setCustomValidity('');
    }
    return true;
}
function Invalidtel2(textbox) {
	
	var tel2 = document.getElementById('phone2').value;
	var tel3 = document.getElementById('phone3').value;
	if( (tel2 == tel3)&&(tel2!="") ){
        textbox.setCustomValidity('Veuillez saisir trois numeros de téléphone différents !');
    }    
    else {
        textbox.setCustomValidity('');
    }
    return true;
}
function checkage() {

	
  var y = document.getElementById('by').value;
  var m = document.getElementById('bm').value;
  var d = document.getElementById('bd').value;
  var dn=new Date();
  
 if ( 
	(((dn.getFullYear())-parseInt(y))<18) ||
	( (((dn.getFullYear())-parseInt(y))==18) && ((dn.getMonth()+1)<parseInt (m)) ) ||
	( (((dn.getFullYear())-parseInt(y))==18) && ((dn.getMonth()+1)==parseInt (m))&& ((dn.getUTCDate())<parseInt(d)) )
    ){
	document.getElementById('lb1').style.display = "block";
	document.getElementById('lb2').style.display = "block";
	document.getElementById('phone2').style.display = "block";
	document.getElementById('phone3').style.display = "block";
	document.getElementById('phone2').required = true;
	document.getElementById('phone3').required = true;
	document.getElementById('stt').value="PB"; 
 }
  else{
	document.getElementById('lb1').style.display = "none";
	document.getElementById('lb2').style.display = "none";
	document.getElementById('phone2').style.display = "none";
	document.getElementById('phone3').style.display = "none"; 
	document.getElementById('phone2').required = "";
	document.getElementById('phone3').required = "";
	document.getElementById('stt').value="GB";
  }
  return true; 
}
function checkcreneau() {

	
  var j = document.getElementById('jr').value;
  var c = document.getElementById('cr');
  var op=c.options[5];
  var opd=c.options[0];
 if ( (j==1)||(j==2)||(j==3)||(j==6)||(j==7) ){
	opd.defaultSelected = true;
	document.getElementById('c9').style.display = "none";
	document.getElementById('c10').style.display = "none"; 
	if( (j==7) ){
	op.defaultSelected = true;
	document.getElementById('c1').style.display = "none";
	document.getElementById('c2').style.display = "none";
	document.getElementById('c3').style.display = "none";
	document.getElementById('c4').style.display = "none";
	document.getElementById('c5').style.display = "none";
	}
 	else 
 	{
	 opd.defaultSelected = true;
	document.getElementById('c1').style.display = "block";
	document.getElementById('c2').style.display = "block";
	document.getElementById('c3').style.display = "block";
	document.getElementById('c4').style.display = "block";
	document.getElementById('c5').style.display = "block";
	}
 }
 else
 {
	document.getElementById('c9').style.display = "block";
	document.getElementById('c10').style.display = "block";	
 }

  return true; 
}
