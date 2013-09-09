<?php
#!/usr/bin/php –q 
#
#
if($_SERVER["argc"]>=2) {
	$file = $_SERVER["argv"][1];

	$input = $_SERVER["argv"][2];
	echo "file:$file  input:$input \n";
	$str = "";
	for($i=0;$i<$input;$i++)
	{
		$str .= $i."\n";
	}

	file_put_contents($file,$str);
	echo "out ok\n";
} else {
	var_dump($_SERVER["argc"]);
}
?>
