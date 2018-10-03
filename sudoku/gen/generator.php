function GenGrille()
{
    $bOk=true;
    $t=array();
    for($i=0;$i<9;$i++)
    {
        $t[$i]=array();
        
        for($j=0;$j<9;$j++)
        {
            $t[$i][$j]=array();
            for($k=0;$k<9;$k++)
            {
                //On génère un tableau de 9*9, avec les 9 possibilités dans chaque case
                $t[$i][$j]['K'.$k]=$k;
            }
        }
    }

    //Apoptose
    for($i=0;$i<9;$i++)
    {
        for($j=0;$j<9;$j++)
        {
            // Si aucune solution dans une case, on recommence
            if(!count($t[$i][$j])) return GenGrille();
            $r=rand(0,count($t[$i][$j])-1);
            $t2=array_keys($t[$i][$j]);
            $t[$i][$j]=$t[$i][$j][$t2[$r]];

            //suppression même ligne
            //suppression même colonne
            //Suppression dans le même carré
            for($z=0;$z<9;$z++)
            {
                unset($t[$z][$j][$t2[$r]]);
                unset($t[$i][$z][$t2[$r]]);
                
                for($zz=0;$zz<9;$zz++){
                    if(floor($z/3)==floor($i/3) && floor($zz/3)==floor($j/3)) unset($t[$z][$zz][$t2[$r]]);

                }
            }
        }
    }
    return $t;
}
