package com.phonegap.sfa;

public class Arabic6822Length {

	  static int rows = 43;
		static int columns = 5;
		static  char[][] CharArray = new char[rows][columns];
		
    
   /**
    * Call this function to convert Arabic in Android to CP864 format for
    * Intermec printers
    * 
    */
   public String ConvertLength(String ArabicIn, boolean ArabicNumbers)
   {

       CharArray[37][0] = (char)'ػ'; CharArray[37][1] = (char)0x00e0; CharArray[37][2] = (char)0x00e0; CharArray[37][3] = (char)0x00e0; CharArray[37][4] = (char)0x00e0;
       CharArray[38][0] = (char)0x0099; CharArray[38][1] = (char)0x0099; CharArray[38][2] = (char)0x009a; CharArray[38][3] = (char)0x0099; CharArray[38][4] = (char)0x0099;
       CharArray[39][0] = (char)0x009d; CharArray[39][1] = (char)0x009e; CharArray[39][2] = (char)0x009d; CharArray[39][3] = (char)0x009e; CharArray[39][4] = (char)0x009d;
       CharArray[40][0] = (char)0x0651; CharArray[40][1] = (char)0x00f1; CharArray[40][2] = (char)0x00f1; CharArray[40][3] = (char)0x00f1; CharArray[40][4] = (char)0x00f1;
       CharArray[0][0] = (char)0x630; CharArray[0][1] = (char)0xd0; CharArray[0][2] = (char)0xd0; CharArray[0][3] = (char)0xd0; CharArray[0][4] = (char)0xd0;
       //CharArray[0][1] = (char)0xfeac; CharArray[0][2] = (char)0xfeab; CharArray[0][3] = (char)0xfeac; CharArray[0][4] = (char)0xfeab;#ARABIC LETTER THAL ISOLATED FORM
       CharArray[1][0] = (char)0x62f; CharArray[1][1] = (char)0xcf; CharArray[1][2] = (char)0xcf; CharArray[1][3] = (char)0xcf; CharArray[1][4] = (char)0xcf;
       //CharArray[1][1] = (char)0xfeaa; CharArray[1][2] = (char)0xfea9; CharArray[1][3] = (char)0xfeaa; CharArray[1][4] = (char)0xfea9;#ARABIC LETTER DAL ISOLATED FORM
       CharArray[2][0] = (char)0x62c; CharArray[2][1] = (char)0xad; CharArray[2][2] = (char)0xcc; CharArray[2][3] = (char)0xcc; CharArray[2][4] = (char)0xad;
       //CharArray[2][1] = (char)0xfe9e; CharArray[2][2] = (char)0xfe9f; CharArray[2][3] = (char)0xfea0; CharArray[2][4] = (char)0xfe9d;#ARABIC LETTER JEEM ISOLATED FORM#ARABIC LETTER JEEM INITIAL FORM
       CharArray[3][0] = (char)0x62d; CharArray[3][1] = (char)0xae; CharArray[3][2] = (char)0xcd; CharArray[3][3] = (char)0xcd; CharArray[3][4] = (char)0xae;
       //CharArray[3][1] = (char)0xfea2; CharArray[3][2] = (char)0xfea3; CharArray[3][3] = (char)0xfea4; CharArray[3][4] = (char)0xfea1;#ARABIC LETTER HAH ISOLATED FORM#ARABIC LETTER HAH INITIAL FORM
       CharArray[4][ 0] = (char)0x62e; CharArray[4][1] = (char)0xaf; CharArray[4][2] = (char)0xce; CharArray[4][3] = (char)0xce; CharArray[4][4] = (char)0xaf;
       //CharArray[4][1] = (char)0xfea6; CharArray[4][2] = (char)0xfea7; CharArray[4][3] = (char)0xfea8; CharArray[4][4] = (char)0xfea5;#ARABIC LETTER KHAH ISOLATED FORM#ARABIC LETTER KHAH INITIAL FORM
       CharArray[5][0] = (char)0x647; CharArray[5][1] = (char)0xf3; CharArray[5][2] = (char)0xe7; CharArray[5][3] = (char)0xf4; CharArray[5][4] = (char)0xf3;
       //CharArray[5][1] = (char)0xfeea; CharArray[5][2] = (char)0xfeeb; CharArray[5][3] = (char)0xfeec; CharArray[5][4] = (char)0xfee9;#ARABIC LETTER HEH INITIAL FORM#ARABIC LETTER HEH ISOLATED FORM#ARABIC LETTER HEH MEDIAL FORM
       CharArray[6][0] = (char)0x639; CharArray[6][1] = (char)0xc5; CharArray[6][2] = (char)0xd9; CharArray[6][3] = (char)0xec; CharArray[6][4] = (char)0xdf;
       //CharArray[6][1] = (char)0xfeca; CharArray[6][2] = (char)0xfecb; CharArray[6][3] = (char)0xfecc; CharArray[6][4] = (char)0xfec9;#ARABIC LETTER AIN FINAL FORM#ARABIC LETTER AIN INITIAL FORM#ARABIC LETTER AIN ISOLATED FORM#ARABIC LETTER AIN MEDIAL FORM
       CharArray[7][0] = (char)0x63a; CharArray[7][1] = (char)0xed; CharArray[7][2] = (char)0xda; CharArray[7][3] = (char)0xf7; CharArray[7][4] = (char)0xee;
       //CharArray[7][1] = (char)0xfece; CharArray[7][2] = (char)0xfecf; CharArray[7][3] = (char)0xfed0; CharArray[7][4] = (char)0xfecd;#ARABIC LETTER GHAIN INITIAL FORM#ARABIC LETTER GHAIN FINAL FORM#ARABIC LETTER GHAIN ISOLATED FORM#ARABIC LETTER GHAIN MEDIAL FORM
       CharArray[8][0] = (char)0x641; CharArray[8][1] = (char)0xba; CharArray[8][2] = (char)0xe1; CharArray[8][3] = (char)0xe1; CharArray[8][4] = (char)0xba;
       //CharArray[8][1] = (char)0xfed2; CharArray[8][2] = (char)0xfed3; CharArray[8][3] = (char)0xfed4; CharArray[8][4] = (char)0xfed1;#ARABIC LETTER FEH ISOLATED FORM#ARABIC LETTER FEH INITIAL FORM
       CharArray[9][0] = (char)0x642; CharArray[9][1] = (char)0xf8; CharArray[9][2] = (char)0xe2; CharArray[9][3] = (char)0xe2; CharArray[9][4] = (char)0xf8;
       //CharArray[9][1] = (char)0xfed6; CharArray[9][2] = (char)0xfed7; CharArray[9][3] = (char)0xfed8; CharArray[9][4] = (char)0xfed5;#ARABIC LETTER QAF INITIAL FORM#ARABIC LETTER QAF ISOLATED FORM
       CharArray[10][0] = (char)0x62b; CharArray[10][1] = (char)0xab; CharArray[10][2] = (char)0xcb; CharArray[10][3] = (char)0xcb; CharArray[10][4] = (char)0xab;
       //CharArray[10][1] = (char)0xfe9a; CharArray[10][2] = (char)0xfe9b; CharArray[10][3] = (char)0xfe9c; CharArray[10][4] = (char)0xfe99;#ARABIC LETTER THEH ISOLATED FORM#ARABIC LETTER THEH INITIAL FORM
       CharArray[11][0] = (char)0x635; CharArray[11][1] = (char)0xbe; CharArray[11][2] = (char)0xd5; CharArray[11][3] = (char)0xd5; CharArray[11][4] = (char)0xbe;
       //CharArray[11][1] = (char)0xfeba; CharArray[11][2] = (char)0xfebb; CharArray[11][3] = (char)0xfebc; CharArray[11][4] = (char)0xfeb9;#ARABIC LETTER SAD ISOLATED FORM#ARABIC LETTER SAD INITIAL FORM
       CharArray[12][0] = (char)0x636; CharArray[12][1] = (char)0xeb; CharArray[12][2] = (char)0xd6; CharArray[12][3] = (char)0xd6; CharArray[12][4] = (char)0xeb;
       //CharArray[12][1] = (char)0xfebe; CharArray[12][2] = (char)0xfebf; CharArray[12][3] = (char)0xfec0; CharArray[12][4] = (char)0xfebd;#ARABIC LETTER DAD INITIAL FORM#ARABIC LETTER DAD ISOLATED FORM
       CharArray[13][0] = (char)0x637; CharArray[13][1] = (char)0xd7; CharArray[13][2] = (char)0xd7; CharArray[13][3] = (char)0xd7; CharArray[13][4] = (char)0xd7;
       //CharArray[13][1] = (char)0xfec2; CharArray[13][2] = (char)0xfec3; CharArray[13][3] = (char)0xfec4; CharArray[13][4] = (char)0xfec1;#ARABIC LETTER TAH INITIAL FORM
       CharArray[14][0] = (char)0x643; CharArray[14][1] = (char)0xfc; CharArray[14][2] = (char)0xe3; CharArray[14][3] = (char)0xe3; CharArray[14][4] = (char)0xfc;
       //CharArray[14][1] = (char)0xfeda; CharArray[14][2] = (char)0xfedb; CharArray[14][3] = (char)0xfedc; CharArray[14][4] = (char)0xfed9;#ARABIC LETTER KAF INITIAL FORM#ARABIC LETTER KAF ISOLATED FORM
       CharArray[15][0] = (char)0x645; CharArray[15][1] = (char)0xef; CharArray[15][2] = (char)0xe5; CharArray[15][3] = (char)0xe5; CharArray[15][4] = (char)0xef;
       //CharArray[15][1] = (char)0xfee2; CharArray[15][2] = (char)0xfee3; CharArray[15][3] = (char)0xfee4; CharArray[15][4] = (char)0xfee1;#ARABIC LETTER MEEM INITIAL FORM#ARABIC LETTER MEEM ISOLATED FORM
       CharArray[16][0] = (char)0x646; CharArray[16][1] = (char)0xf2; CharArray[16][2] = (char)0xe6; CharArray[16][3] = (char)0xe6; CharArray[16][4] = (char)0xf2;
       //CharArray[16][1] = (char)0xfee6; CharArray[16][2] = (char)0xfee7; CharArray[16][3] = (char)0xfee8; CharArray[16][4] = (char)0xfee5;#ARABIC LETTER NOON INITIAL FORM#ARABIC LETTER NOON ISOLATED FORM
       CharArray[17][0] = (char)0x62a; CharArray[17][1] = (char)0xaa; CharArray[17][2] = (char)0xca; CharArray[17][3] = (char)0xaa; CharArray[17][4] = (char)0xaa;
       //CharArray[17][1] = (char)0xfe96; CharArray[17][2] = (char)0xfe97; CharArray[17][3] = (char)0xfe98; CharArray[17][4] = (char)0xfe95;#ARABIC LETTER TEH ISOLATED FORM#ARABIC LETTER TEH INITIAL FORM
       CharArray[18][0] = (char)0x627; CharArray[18][1] = (char)0xa8; CharArray[18][2] = (char)0xc7; CharArray[18][3] = (char)0xa8; CharArray[18][4] = (char)0xc7;
       //CharArray[18][1] = (char)0xfe8e; CharArray[18][2] = (char)0xfe8d; CharArray[18][3] = (char)0xfe8e; CharArray[18][4] = (char)0xfe8d;#ARABIC LETTER ALEF FINAL FORM#ARABIC LETTER ALEF ISOLATED FORM
       CharArray[19][0] = (char)0x644; CharArray[19][1] = (char)0xfb; CharArray[19][2] = (char)0xe4; CharArray[19][3] = (char)0xe4; CharArray[19][4] = (char)0xfb;
       //CharArray[19][1] = (char)0xfede; CharArray[19][2] = (char)0xfedf; CharArray[19][3] = (char)0xfee0; CharArray[19][4] = (char)0xfedd;#ARABIC LETTER LAM INITIAL FORM#ARABIC LETTER LAM ISOLATED FORM
       CharArray[20][0] = (char)0x628; CharArray[20][1] = (char)0xa9; CharArray[20][2] = (char)0xc8; CharArray[20][3] = (char)0xc8; CharArray[20][4] = (char)0xa9;
       //CharArray[20][1] = (char)0xfe90; CharArray[20][2] = (char)0xfe91; CharArray[20][3] = (char)0xfe92; CharArray[20][4] = (char)0xfe8f;#ARABIC LETTER BEH ISOLATED FORM#ARABIC LETTER BEH INITIAL FORM
       CharArray[21][0] = (char)0x64a; CharArray[21][1] = (char)0xf6; CharArray[21][2] = (char)0xea; CharArray[21][3] = (char)0xea; CharArray[21][4] = (char)0xfd;
       //CharArray[21][1] = (char)0xfef2; CharArray[21][2] = (char)0xfef3; CharArray[21][3] = (char)0xfef4; CharArray[21][4] = (char)0xfef1;#ARABIC LETTER YEH INITIAL FORM#ARABIC LETTER YEH FINAL FORM#ARABIC LETTER YEH ISOLATED FORM
       CharArray[22][0] = (char)0x633; CharArray[22][1] = (char)0xbc; CharArray[22][2] = (char)0xd3; CharArray[22][3] = (char)0xd3; CharArray[22][4] = (char)0xbc;
       //CharArray[22][1] = (char)0xfeb2; CharArray[22][2] = (char)0xfeb3; CharArray[22][3] = (char)0xfeb4; CharArray[22][4] = (char)0xfeb1;#ARABIC LETTER SEEN ISOLATED FORM#ARABIC LETTER SEEN INITIAL FORM
       CharArray[23][0] = (char)0x634; CharArray[23][1] = (char)0xbd; CharArray[23][2] = (char)0xd4; CharArray[23][3] = (char)0xd4; CharArray[23][4] = (char)0xbd;
       //CharArray[23][1] = (char)0xfeb6; CharArray[23][2] = (char)0xfeb7; CharArray[23][3] = (char)0xfeb8; CharArray[23][4] = (char)0xfeb5;#ARABIC LETTER SHEEN ISOLATED FORM#ARABIC LETTER SHEEN INITIAL FORM
       CharArray[24][0] = (char)0x638; CharArray[24][1] = (char)0xd8; CharArray[24][2] = (char)0xd8; CharArray[24][3] = (char)0xd8; CharArray[24][4] = (char)0xd8;
       //CharArray[24][1] = (char)0xfec6; CharArray[24][2] = (char)0xfec7; CharArray[24][3] = (char)0xfec8; CharArray[24][4] = (char)0xfec5;#ARABIC LETTER ZAH INITIAL FORM
       CharArray[25][0] = (char)0x632; CharArray[25][1] = (char)0xd2; CharArray[25][2] = (char)0xd2; CharArray[25][3] = (char)0xd2; CharArray[25][4] = (char)0xd2;
       //CharArray[25][1] = (char)0xfeb0; CharArray[25][2] = (char)0xfeaf; CharArray[25][3] = (char)0xfeb0; CharArray[25][4] = (char)0xfeaf;#ARABIC LETTER ZAIN ISOLATED FORM
       CharArray[26][0] = (char)0x648; CharArray[26][1] = (char)0xe8; CharArray[26][2] = (char)0xe8; CharArray[26][3] = (char)0xe8; CharArray[26][4] = (char)0xe8;
       //CharArray[26][1] = (char)0xfeee; CharArray[26][2] = (char)0xfeed; CharArray[26][3] = (char)0xfeee; CharArray[26][4] = (char)0xfeed;#ARABIC LETTER WAW ISOLATED FORM
       CharArray[27][0] = (char)0x629; CharArray[27][1] = (char)0xc9; CharArray[27][2] = (char)0xc9; CharArray[27][3] = (char)0xc9; CharArray[27][4] = (char)0xc9;
       //CharArray[27][1] = (char)0xfe94; CharArray[27][2] = (char)0xfe93; CharArray[27][3] = (char)0xfe93; CharArray[27][4] = (char)0xfe93;#ARABIC LETTER TEH MARBUTA ISOLATED FORM
       CharArray[28][0] = (char)0x649; CharArray[28][1] = (char)0xf5; CharArray[28][2] = (char)0xe9; CharArray[28][3] = (char)0xf5; CharArray[28][4] = (char)0xe9;
       //CharArray[28][1] = (char)0xfef0; CharArray[28][2] = (char)0xfeef; CharArray[28][3] = (char)0xfef0; CharArray[28][4] = (char)0xfeef;#ARABIC LETTER ALEF MAKSURA ISOLATED FORM#ARABIC LETTER ALEF MAKSURA FINAL FORM
       CharArray[29][0] = (char)0x631; CharArray[29][1] = (char)0xd1; CharArray[29][2] = (char)0xd1; CharArray[29][3] = (char)0xd1; CharArray[29][4] = (char)0xd1;
       //CharArray[29][1] = (char)0xfeae; CharArray[29][2] = (char)0xfead; CharArray[29][3] = (char)0xfeae; CharArray[29][4] = (char)0xfead;#ARABIC LETTER REH ISOLATED FORM
       CharArray[30][0] = (char)0x624; CharArray[30][1] = (char)0xc4; CharArray[30][2] = (char)0xc4; CharArray[30][3] = (char)0xc4; CharArray[30][4] = (char)0xc4;
       //CharArray[30][1] = (char)0xfe86; CharArray[30][2] = (char)0xfe85; CharArray[30][3] = (char)0xfe86; CharArray[30][4] = (char)0xfe85;#ARABIC LETTER WAW WITH HAMZA ABOVE ISOLATED FORM
       CharArray[31][0] = (char)0x621; CharArray[31][1] = (char)0xc1; CharArray[31][2] = (char)0xc3; CharArray[31][3] = (char)0xa5; CharArray[31][4] = (char)0xc1;
       //            CharArray[31][0] = (char)0x621; CharArray[31][1] = (char)0xc1; CharArray[31][2] = (char)0xc1; CharArray[31][3] = (char)0xc1; CharArray[31][4] = (char)0xc1;
       //CharArray[31][1] = (char)0xfe80; CharArray[31][2] = (char)0xfe80; CharArray[31][3] = (char)0xfe80; CharArray[31][4] = (char)0xfe7f;#ARABIC LETTER HAMZA ISOLATED FORM
       CharArray[32][0] = (char)0x626; CharArray[32][1] = (char)0xc6; CharArray[32][2] = (char)0xc6; CharArray[32][3] = (char)0xc6; CharArray[32][4] = (char)0xc6;
       //            CharArray[32][0] = (char)0x626; CharArray[32][1] = (char)0xfe8a; CharArray[32][2] = (char)0xc6; CharArray[32][3] = (char)0xc6; CharArray[32][4] = (char)0xfe89;
       //CharArray[32][1] = (char)0xfe8a; CharArray[32][2] = (char)0xfe8b; CharArray[32][3] = (char)0xfe8c; CharArray[32][4] = (char)0xfe89;#ARABIC LETTER YEH WITH HAMZA ABOVE INITIAL FORM
       CharArray[33][0] = (char)0x623; CharArray[33][1] = (char)0xa5; CharArray[33][2] = (char)0xc3; CharArray[33][3] = (char)0xa5; CharArray[33][4] = (char)0xc3;
       //CharArray[33][1] = (char)0xfe84; CharArray[33][2] = (char)0xfe83; CharArray[33][3] = (char)0xfe84; CharArray[33][4] = (char)0xfe83;#ARABIC LETTER ALEF WITH HAMZA ABOVE FINAL FORM#ARABIC LETTER ALEF WITH HAMZA ABOVE ISOLATED FORM
       CharArray[34][0] = (char)0x622; CharArray[34][1] = (char)0xa2; CharArray[34][2] = (char)0xc2; CharArray[34][3] = (char)0xa2; CharArray[34][4] = (char)0xc2;
       //CharArray[34][1] = (char)0xfe82; CharArray[34][2] = (char)0xfe81; CharArray[34][3] = (char)0xfe82; CharArray[34][4] = (char)0xfe81;#ARABIC LETTER ALEF WITH MADDA ABOVE FINAL FORM#ARABIC LETTER ALEF WITH MADDA ABOVE ISOLATED FORM
       CharArray[35][0] = (char)0x625; CharArray[35][1] = (char)0xfe88; CharArray[35][2] = (char)0xfe87; CharArray[35][3] = (char)0xfe88; CharArray[35][4] = (char)0xfe87;
       //CharArray[35][1] = (char)0xfe88; CharArray[35][2] = (char)0xfe87; CharArray[35][3] = (char)0xfe88; CharArray[35][4] = (char)0xfe87;
       CharArray[36][0] = (char)0xfefb; CharArray[36][1] = (char)0x9d; CharArray[36][2] = (char)0x9e; CharArray[36][3] = (char)0x9e; CharArray[36][4] = (char)0x9d;
//       CharArray[36][0] = (char)0xfefb; CharArray[36][1] = (char)0xfef9; CharArray[36][2] = (char)0xfef9; CharArray[36][3] = (char)0x9d; CharArray[36][4] = (char)0x9d;
       //CharArray[36][1] = (char)0xfef9; CharArray[36][2] = (char)0xfef9; CharArray[36][3] = (char)0xfefb; CharArray[36][4] = (char)0xfefb;#ARABIC LIGATURE LAM WITH ALEF ISOLATED FORM
       CharArray[41][0] = (char)0xf9; CharArray[41][1] = (char)0xf9; CharArray[41][2] = (char)0xfa; CharArray[41][3] = (char)0xf9; CharArray[41][4] = (char)0xf9;
       CharArray[42][0] = (char)0x64b; CharArray[42][1] = (char)0xf1; CharArray[42][2] = (char)0xf1; CharArray[42][3] = (char)0xf1; CharArray[42][4] = (char)0xf1;

//	   WriteLog(ArabicIn);
	   
       String arabicout = ArabicIn.trim();

       boolean putBefore, putAfter, foundch;
       foundch = false;
       int i = 0;
       int rowfound = -1;
       int columnfound = -1;
       
           String replacedstr = "";
           for (i = 0; i < ArabicIn.trim().length(); i++)
           {
           	
               if (ArabicIn.trim().charAt(i) == (char)'ل')
               {
                   if (i != ArabicIn.trim().length() - 1)
                   {
                       if ((ArabicIn.trim().charAt(i+1) == (char)'ا'))
                       {
                           replacedstr = replacedstr + (char)0x009d;
                           i++;
                       }
                       else if ((ArabicIn.trim().charAt(i+1) == (char)'إ'))
                       {
                           replacedstr = replacedstr + (char)0x0099;
                           i++;
                       }
                       else if ((ArabicIn.trim().charAt(i+1)== (char)'آ'))
                       {
                           replacedstr = replacedstr + (char)0x00f9;
                           i++;
                       }
                       else if ((ArabicIn.trim().charAt(i+1) == (char)'أ'))
                       {
                           replacedstr = replacedstr + (char)0x0099;
                           i++;
                       }

                       else
                       {
                           replacedstr = replacedstr + ArabicIn.trim().charAt(i);
                       }
                   }
                   else
                   {
                       replacedstr = replacedstr + ArabicIn.trim().charAt(i);
                       //To take care of Lam J at the end of word if it is alone. previous version had left this char
                   }
               }
               else
               {
                   replacedstr = replacedstr + ArabicIn.trim().charAt(i);
               }
           }


           ArabicIn = replacedstr;
           arabicout = replacedstr.trim();
           byte[] tarray= new byte[arabicout.length()];
           for (i = 0; i < ArabicIn.trim().length(); i++)
           {

               foundch = false;
               rowfound = -1;
               columnfound = -1;
               putBefore = false;
               putAfter = false;
               for (int row = 0; row < rows; row++)
               {
                   for (int column = 0; column < columns; column++)
                   {
                       if (CharArray[row][column] == ArabicIn.charAt(i))
                       {
                           foundch = true;
                           rowfound = row;
                           columnfound = column;
                           break;
                       }
                   }
                   if (foundch)
                       break;
               }
               if (foundch)
               {
                   if (i == ArabicIn.trim().length() - 1)
                       putAfter = false;
                   else
                       putAfter = (isJoining1(ArabicIn.trim().charAt(i+1)) ||
                                      isJoining2(ArabicIn.trim().charAt(i+1)));

                   if (i == 0)
                       putBefore = false;
                   else
                       putBefore = isJoining1(ArabicIn.trim().charAt(i-1));
                   if (putBefore && putAfter)
                   {
                   	arabicout.replace(arabicout.charAt(i), CharArray[rowfound][3]);
                      tarray[i]=(byte)CharArray[rowfound][3];
                   }
                   if (putBefore && !putAfter)
                   {
                   	arabicout.replace(arabicout.charAt(i), CharArray[rowfound][1]);
                       tarray[i]=(byte)CharArray[rowfound][1];
                   }
                   if (!putBefore && putAfter)
                   {
                   	arabicout.replace(arabicout.charAt(i), CharArray[rowfound][2]);
                       tarray[i]=(byte)CharArray[rowfound][2];
                   }
                   if (!putBefore && !putAfter)
                   {
                   	arabicout.replace(arabicout.charAt(i), CharArray[rowfound][4]);
                       tarray[i]=(byte)CharArray[rowfound][4];
                   }
               }
               else
               	tarray[i]=(byte) arabicout.charAt(i);

           }
  //         	WriteLog(tarray);
           	
       	  byte[] arafinal = ReverseTest(tarray,ArabicNumbers);
       	 
       	  byte[] bytefor6822 = new byte[(arafinal.length*3)+arafinal.length];
       	  int j=0;
       	  for(i=0; i<arafinal.length;i++)
       	  {
       		  
       		  bytefor6822[j]=(byte)0x1b;
       		  bytefor6822[j+1]=(byte)0x2b;
       		  bytefor6822[j+2]=(byte)0x01;
     		  bytefor6822[j+3]=arafinal[i];
       		  j=j+4;
       	  }
       	  return replacedstr;
       	  
      // 	  return arafinal;

   }
   
      
   public byte[] GetBytes(String src)
   {
   	char[] sourcechar = src.toCharArray();
         byte[] sourcebyte = new byte[sourcechar.length];
         for (int index = 0; index < sourcebyte.length; ++index)
         {
       	  sourcebyte[index] = (byte)sourcechar[index];
       	  
         }
         return sourcebyte;
   	
   }


   boolean isJoining1(char indich)
   {
       char[] GlyphL = new char[] { 
    (char)0x62c, (char)0x62d, (char)0x62e, (char)0x647, (char)0x639, (char)0x63a, (char)0x641, (char)0x642,
   (char)0x62b, (char)0x635, (char)0x636, (char)0x637, (char)0x643, (char)0x645, (char)0x646, (char)0x62a,
   (char)0x644, (char)0x628, (char)0x64a, (char)0x633, (char)0x634, (char)0x638, (char)0x626, (char)0x9d};

       for (int i = 0; i < 24; i++)
       {
           if (indich == GlyphL[i])
           {
               return true;
           }
       }
       return false;
   }

   boolean isJoining2(char indich)
   {
       char[] GlyphL = new char[] { 
    (char)0x627,(char)0x623, (char)0x625, (char)0x622, (char)0x62f, (char)0x630, 
   (char)0x631, (char)0x632, (char)0x648, (char)0x624, (char)0x629, (char)0x649,
   (char)0x626,(char)0xfefb, (char)0xfef9, (char)0xfef7, (char)0xfef5};
       for (int i = 0; i < 17; i++)
       {
           if (indich == GlyphL[i])
           {
               return true;
           }
       }
       return false;
   }




   private byte[] Reverse(byte[] srcbyte)
   {
   	byte[] endbyte= new byte[srcbyte.length];
   	for(int i=0; i<srcbyte.length;i++)
   		endbyte[i]=srcbyte[srcbyte.length-1-i];
   	return endbyte;
   }

   boolean IsNumber(char ch)
   {
       boolean aranum = false;
       char[] GlyphL = new char[] { 
   (char)0x0030, (char)0x0031, (char)0x0032, (char)0x0033,(char)0x0034,(char)0x0035,
       (char)0x0036,(char)0x0037,(char)0x0038,(char)0x0039, (char)0x0660, (char)0x0661, (char)0x0662, (char)0x0663,(char)0x0664,(char)0x0665,
           (char)0x0666, (char)0x0667,(char)0x0668,(char)0x0669,(char)0x002e}; // including period(.)
       for (int i = 0; i < 21; i++)
       {
           if (ch == GlyphL[i])
           {
               aranum= true;
               i = 22;
           }
       }
       return aranum;


   }
   boolean IsNumber(byte ch)
   {
       boolean aranum = false;
        byte[] number = new byte[] {(byte)'1',(byte)'2',(byte)'3',(byte)'4',(byte)'5',(byte)'6',(byte)'7',(byte)'8',(byte)'9',(byte)'0',(byte)'١',(byte)'٢',(byte)'٣',(byte)'٤',(byte)'٥',(byte)'٦',(byte)'٧',(byte)'٨',(byte)'٩',(byte)'٠'};
       for (int i = 0; i < number.length; i++)
       {
           if (ch == number[i])
           {
               aranum= true;
               i = number.length;
           }
       }
       return aranum;


   }

   boolean CheckChar(byte byt)
   {
   	boolean found=false;
   	
   	char[] latin = new char[] {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O'
   			,'P','Q','R','S','T','U','V','W','X','Y','Z','a','b','c','d','e','f','g','h'
   			,'i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','0','1','2','3','4','5','6','7','8','9'
   			,'~','!','@','#','$','%','^','&','*','(',')','-','_','+','=','{','}','[',']','\\','|',':',';'
   			,'<','>',',','.','/','?','١','٢','٣','٤','٥','٦','٧','٨','٩','٠'};
   	
   	for (int i = 0; i < latin.length; i++)
       {
           if ((char)byt == latin[i])
           {
               found= true;
               i = latin.length;
               
           }
       }
   if (!found)
   {
   	byte[] hindinumerals = new byte[] { (byte)'١',(byte)'٢',(byte)'٣',(byte)'٤',(byte)'٥',(byte)'٦',(byte)'٧',(byte)'٨',(byte)'٩',(byte)'٠'};
   	for (int i = 0; i < hindinumerals.length; i++)
       {
           if (byt == hindinumerals[i])
           {
               found= true;
               i = hindinumerals.length;
               
           }
       }


   }
   	return found;

   }

   private byte[] ReverseTest(byte[] ArabicIn, boolean ArabicNumbers)
   {
   	boolean startposset=false;

   	byte[] ArrayOld=Reverse(ArabicIn);
   	
   	byte[] ArrayNew= ArrayOld.clone();
   	int startpos=0;
   	
   int charcount=0;
   	for (int i=0; i<ArrayOld.length; i++)
   	{
   		if (CheckChar(ArrayOld[i]))
   		{
   			charcount++;
   			if(!startposset)
   			{
   				startpos=i;
   				startposset=true;
   			}
   		}
   		else
   		{

   			if (((i-startpos)>1) && (startposset))
   			{
   				int pos=0;
   				int replacepos=i-1;
   				for ( replacepos=i-1; replacepos>=startpos; replacepos--)
   				{
   					ArrayNew[replacepos]=ArrayOld[startpos+pos];
   					pos++;
   				}
   			}
   			startposset=false;
   		}	
   			
   		
   	}
   	if (charcount==ArrayOld.length)
   	{
   		//all are non arabic chars. So reverse them
   		startpos=0;
   		int pos=0;
   		
   		for (int replacepos=ArrayOld.length-1; replacepos>=0; replacepos--)
   		{
   			ArrayNew[replacepos]=ArrayOld[startpos+pos];
   			
   			pos++;
   		}
   	}
   		byte[] numberreplace= ReplaceNumbers(ArrayNew,ArabicNumbers);
   		return numberreplace;
   	
   }

   byte[] ReplaceNumbers(byte[] in, boolean AraNum)
   {
   	for (int i=0; i<in.length;i++)
   	{
   		if (IsNumber(in[i]))
   		{
   			   if( in[i] == (byte)'0' || in[i] == (byte)'٠')
                  {
   				   if (AraNum)
                      {
                       in[i] = (byte)0x00b0;
                      }
   				   else
   					   in[i] = (byte)0x0030;
                  }
                  else if( in[i] == (byte)'1'||in[i] == (byte)'١')
                  {
               	   if (AraNum)
                      {
                          in[i] = (byte)0x00b1;
                      }
                      else { in[i] = (byte)0x0031; }
                  }
                  else if( in[i] == (byte)'2'||in[i] ==(byte)'٢')
                  {
                      if (AraNum)
                      {
                          in[i] =(byte)0x00b2;
                      }
                      else { in[i] =(byte)0x0032; }
                  }
                  else if( in[i] == (byte)'3'||in[i] ==(byte)'٣')
                  {
                      if (AraNum)
                      {
                          in[i] =(byte)0x00b3;
                      }
                      else { in[i] =(byte)0x0033; }
                  }
                  else if( in[i] == (byte)'4'||in[i] ==(byte)'٤')
                  {
                      if (AraNum)
                      {
                          in[i] =(byte)0x00b4;
                      }
                      else { in[i] =(byte)0x0034; }
                  }
                  else if( in[i] == (byte)'5'||in[i] ==(byte)'٥')
                  {
                      if (AraNum)
                      {
                          in[i] =(byte)0x00b5;
                      }
                      else { in[i] =(byte)0x0035; }
                  }
                  else if(in[i] == (byte)'6'||in[i] ==(byte)'٦')
                  {
                      if (AraNum)
                      {
                          in[i] =(byte)0x00b6;
                      }
                      else { in[i] =(byte)0x0036; }
                  }
                  else if( in[i] == (byte)'7'||in[i] ==(byte)'٧')
                  {
                      if (AraNum)
                      {
                          in[i] =(byte)0x00b7;
                      }
                      else { in[i] =(byte)0x0037; }
                  }
                  else if(in[i] == (byte)'8'||in[i] ==(byte)'٨')
                  {
                      if (AraNum)
                      {
                          in[i] =(byte)0x00b8;
                      }
                      else { in[i] =(byte)0x0038; }
                  }
                  else if(in[i] == (byte)'9'||in[i] ==(byte)'٩')
                  {
                      if (AraNum)
                      {
                          in[i] =(byte)0x00b9;
                      }
                      else { in[i] =(byte)0x0039; }
                  }
   			
   		}
   		

   		
   	}
   	return in;
   }



	
}
