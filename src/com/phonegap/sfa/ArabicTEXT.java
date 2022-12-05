package com.phonegap.sfa;

import java.util.HashMap;

public class ArabicTEXT {
	private static HashMap<String, String> hashArabValues;
	public static final String NetAmount = "كمية الشبكة";
	public static final String PRODUCTIVITY = "إنتاجية";
	public static final String STRIKERATE = "معدل إضراب";
	public static final String RouteSummary = "الطريق ملخص";
	public static final String Target = "الهدف :";
	public static final String DataSet = "بيانات";
	public static String DepartmentHead = "رئيس القسم";
	public static String Sales="مبيعات";
	public static String Order="طلب";
	public static String tradeDeal="مجانا";
	public static String PromotionFree="العرض المجاني";
	public static String BuybackFree="تعزيز الحرة";
	public static String GoodReturn="مرتجعات صالحة";
	public static String BadReturn="مرتجعات تالفة";
	public static String Customer="توقيع العميل";
	public static String Salesman="بائع";
	public static String AmtBeforeVat="المبلغ قبل ضريبة القيمة المضافة";
	public static String RetAmt="عائد المبلغ";
	public static String SalesAmt="مبلغ المبيعات";
	public static String SubTotal="الإجمالى";
	public static String AmtAfterVat="المبلغ بعد ضريبة القيمة المضافة";
	public static String NetSales="صافي المبيعات";
	public static String InvoiceDiscount="خصم الفاتورة";
	public static String OrderDiscount="من اجل تخفيض";
	public static String TCcharged="الدين المؤقت";
	public static String OriginalCopy="نسخة أصلية";
	public static String DuplicateCopy="نسخة مكررة";
	public static String DraftCopy="نسخة غير نهائية";
	public static String headerArabic="*"+"الصنف  "+"الصنف                         "+"   "+"          الكمية    "+" الباكيت"+"  الحبة"+"الخصم    "+"القيمة   "+"!";
//	public static String headerrevereseArabic="*"+"   القيمة"+"    الخصم"+"  الحبة"+" الباكيت"+"  الكمية"+"  "+"                         الصنف"+"   الصنف"+"!";
//	public static String headerbottomrevereseArabic="*"+"        "+"        "+"سعر    "+"سعر    "+"      "+"  "+"                           وصف"+"     رقم"+"!";
//	
	public static String headerrevereseArabic="*"+"   القيمة"+"                          الحبة"+"   الباكيت"+"       الكمية"+"  "+"               "+" الصنف"+"!";
	public static String headerbottomrevereseArabic="*"+"        "+"سعر    "+"سعر    "+"      "+"  "+"                     "+"   رقم"+"!";
	
	public static String headerminirevereseArabic="*"+"الكمية      "+"      "+"                                          الصنف"+"      الصنف"+"!";
	public static String headerminibottomrevereseArabic="*"+"               "+"      "+"                                              وصف"+"        رقم"+"!";
	
	//public static String headerDotmatrevereseArabic="*"+"القيمة"+"                "+"      الحبة"+"   الباكيت"+"  الكمية "+"  الوحدات"+"                                                      الصنف"+"     الصنف"+"   ."+"!";
	public static String headerDotmatrevereseArabic="*" +"      كمية     "+" ضريبة "+"المبلغ قبل الضريبة "+"خصم"+"  سعر الوحده "+"سعر الحالة "+" كمية   "+" وحدة"+"                              وصف"+"           "+"الباركود"+"العنصر"+""+"!";
    public static String headerDotmatbottomrevereArab="*"+"      "+"                "+"        سعر"+"       سعر"+"         "+"         "+"                                                          وصف"+"       رقم"+"   ."+"!";
	
    //For Hiding Prices 
    public static String headerminiDotmatrevereseArabic="*"+"المجموع"+"   الوحدات"+"                                                                                            الصنف"+"          الصنف"+"    ."+"!";
    public static String headerminiDotmatbottomrevereArabic="*"+"       "+"          "+"                                                                                              وصف"+"            رقم"+"    ."+"!";
	
    
	public static String Cashinvoice="فاتورةكاش";
	public static String EMAIL="البريد الإلكتروني";
	public static String Creditinvoice="فاتورةأجل";
	public static String Comment="تعليق";
	
	
	public static String ROUTE="مسار";
	public static String DATE="تاريخ";
	public static String SALESMAN="بائع";
	public static String TIME="مرة";
	
	public static String Item="رقم الصنف";
	public static String OUTLET="OUTLET CODE";
	public static String DESCRIPTION="وصف الصنف";
	public static String UPC="الوحدات";
	public static String QTY="الكمية";
	
	public static String TOTAL="المجموع";
	public static String CASEPRICE="سعر الباكيت";
	public static String UNITPRICE="سعر الحبة";
	public static String DISCOUNT="الخصم";
	public static String AMOUNT="القيمة";
	public static String Receipt="سند تحصيل";
	
	public static String PaymentDetails="تفاصيل المدفوعات";
	public static String Total="الإجمالى";
	public static String orderexVat="المبلغ غير شامل لضريبة القيمة المضافة";
	public static String Invoice="رقم الفاتورة";
	public static String InvoiceDate=" تاريخ الفاتورة";
	public static String DueDate="تاريخ استحقاق الفاتورة";
	public static String InvoiceAmount="قيمة الفاتورة";
	public static String AmountPaid=" المبلغ المدفوع";
	public static String InvoiceBalance="رصيد الفاتورة";
	public static String Cash="نقدا";
	public static String TargetVsGoals = "استهداف مقابل الأهداف";
	
	public static String CustomerBalance="جمالي الديون";
	public static String TotalSalesAmount="إجمالي مبلغ المبيعات";
	public static String TotalGoodReturn="اجمالي رجيع صالح";
	public static String TotalBadReturn="اجمالي رجيع تالف";
	public static String NetInvoiceAmount="صافي مبلغ المبيعات";
	public static String Tax="ضريبة";
	public static  String StoreKeeper = "امين المخزن";
	public static String exciseNumber="رقم المكوس";
	public static final String FROMSALESMAN = "من بائع";
	public static final String TOSALESMAN = "إلى بائع";
	public static final String SUPERVISOR = "مشرف";
	public static final String Discount = "خصم";
	
	public static final String taxdetail = "رقم الضريبية";
	public static final String taxInv = "فاتورة ضريبية";
	public static final String invNo = "رقم الفاتورة.";
	public static final String creditNoteNo="رقم المذكرة الائتمانية";
	public static final String TotalTax="مجموع الضريبة";
	public static final String address="عنوان";
	public static final String contact="اتصل";
	public static final String taxRegNo="رقم ضريبة العميل";
	public static final String customerid="رقم العميل";
	public static final String customername="اسم";
	public static final String taxOrder = "النظام الضريبي";
	public static final String orderNo = "أمر رقم.";
	
	public static final String TotalExcTax="مجموع ضريبة الضرائب";
	public static final String TotalVatTax="قيمة الضريبة المضافة";
	public static final String credit="آجل";
	public static final String cheque="التحقق من";
	public static final String sig="التوقيع";
	public static final String checkdate="تاريخ الشيك";
	public static final String checkNum="رقم الشيك";
	public static final String bank="بنك";
	public static final String amount="كمية";
	
	public static final String routeArabic="رقم المسار";
	public static final String deliveryDate="تاريخ التسليم" ;
	public static final String invoicedate="تاريخ الفاتورة";
	public static final String tourId="رقم الرحلة";
	public static final String SIGN="التوقيع";
	private static final String Barcode = "الباركود";
	private static final String OUTLETCODE = "رمز منفذ";
	private static final String GROSSAMOUNT = "المبلغ الإجمالي";
	private static final String EXCISETAX = "الضريبة";
	private static final String VATAMOUNT = "ضريبة";
	private static final String ARABICDESCRIPTION = "وصف العربية";
	public static final String CREDITAMOUNT = "مبلغ الائتمان";
	public static String getCollectionHeader(boolean isHeader){
		
		
		
		String header="*"+"الفاتورة          "+"المدفوع            "+"الفاتورة          "+"الفاتورة       "+"الفاتورة           "+"!";
		
		
		StringBuilder sbBottom=new StringBuilder();
		sbBottom.append("*");
		sbBottom.append("رصيد            ");
		sbBottom.append("المبلغ          ");
		sbBottom.append("قيمة            ");
		sbBottom.append("تاريخ           ");
		sbBottom.append("رقم                 ");
		sbBottom.append("!");
		
		return isHeader?header:sbBottom.toString();
	}
	
	public static String getSignature(){
    	String HeaderVal="";
    	
    	 HeaderVal="*"+"  التوقيع SIGN./SATMP   "+"  توقيع العميل  BY RECEIVED   "+"   بائع  SALESMAN   "+"!";
    	 
		return HeaderVal;
    }
	
	public static String getPaymentHeader(){
    	String HeaderVal="";
    	
    	 HeaderVal="*"+"كمية"+"                              بنك"+"           رقم الشيك"+"     تاريخ الاختيار"+"!";
    	 
		return HeaderVal;
    }
	
    public static String getArabicHeaderDotmat(double excTot, double vatTot, int totaldiscount){
    	String HeaderVal="";
    	if(excTot>0&&vatTot>0){
    		HeaderVal="*"+"كمية   "+"برميل"+"غير شامل"+" يس السعر"+"  سعر القضية"+"   الكمية"+"  وحدة"+" وصف                         "+"                              وصف"+"       #بند"+"sl#"+"!";
    	 }else if(excTot>0){
    		 HeaderVal="*"+"كمية    "+"   غير شامل"+"  يس السعر"+"  سعر القضية"+"   الكمية"+"  وحدة"+" وصف                         "+"                              وصف"+"       #بند"+"sl#"+"!";
    	 }else if(vatTot>0){
    		 HeaderVal="*"+"كمية    "+"      ضريبة"+"  يس السعر"+"  سعر القضية"+"   الكمية"+"  وحدة"+" وصف                         "+"                              وصف"+"       #بند"+"sl#"+"!";
    	 }else{
    		 HeaderVal="*"+"كمية     "+"         "+"  يس السعر"+"  سعر القضية"+"   الكمية"+"  وحدة"+" وصف                         "+"                              وصف"+"       #بند"+"sl#"+"!";
    	 }

		return HeaderVal;
    }
    
    public static String getArabicHeaderPB51(double excTot, double vatTot, int totaldiscount){
    	String HeaderVal="";
    	
    	if(excTot>0&&vatTot>0){
    		HeaderVal="*"+"كمية   "+"برميل"+"غير شامل"+"  يس السعر"+"  سعر القضية"+"   الكمية"+" وحدة"+"       "+"       #بند"+"sl#"+"!";
    	 }else if(excTot>0){
    		 HeaderVal="*"+"كمية   "+"   غير شامل"+"  يس السعر"+"  سعر القضية"+"   الكمية"+" وحدة"+"       "+"       #بند"+"sl#"+"!";
    	 }else if(vatTot>0){
    		 HeaderVal="*"+"كمية   "+"      برميل"+"  يس السعر"+"  سعر القضية"+"   الكمية"+" وحدة"+"       "+"       #بند"+"sl#"+"!";
    	 }else{
    		 HeaderVal="*"+"كمية   "+"         "+"  يس السعر"+"  سعر القضية"+"   الكمية"+" وحدة"+"       "+"       #بند"+"sl#"+"!";
    	 }
    	


		return HeaderVal;
    }

	public static String getHeaderVal(String HeaderVal){
		
		
//		if(HeaderVal.contains("CASE")){
//			HeaderVal=HeaderVal.replace("CASE", "OUTER");
//			
//		}else if(HeaderVal.contains("UNIT")){
//			HeaderVal=HeaderVal.replace("UNIT", "PCS");
//			
//		}else if(HeaderVal.contains("CAS")){
//			HeaderVal=HeaderVal.replace("CAS", "OUT");
//			
//		}
		return HeaderVal;
	}
	public static String getItemHeaders(int printoutlet,boolean isHeader,boolean isMini,boolean isDotmat){
		String headerItem="",outltecode="";
		if(isDotmat){
			outltecode="               ";
			if(!isMini){
				if(isHeader){
					headerItem="*"+"القيمة"+"             "+"     الحبة"+"   الباكيت"+"  الكمية "+"  الوحدات"+(printoutlet==1?"":outltecode)+"                                            الصنف"+(printoutlet==1?outltecode:"")+"     الصنف"+"   ."+"!";
				}else{
					headerItem="*"+"      "+"             "+"       سعر"+"       سعر"+"         "+"         "+(printoutlet==1?"":outltecode)+"                                              وصف"+(printoutlet==1?outltecode:"")+"       رقم"+"   ."+"!";
				}
			}else{
				if(isHeader){
					headerItem="*"+"المجموع"+"   الوحدات"+(printoutlet==1?"":outltecode)+"                                                                               الصنف"+(printoutlet==1?outltecode:"")+"      الصنف"+"    ."+"!";
				}else{
					headerItem="*"+"       "+"          "+(printoutlet==1?"":outltecode)+"                                                                                 وصف"+(printoutlet==1?outltecode:"")+"        رقم"+"    ."+"!";
				}
			}
			
		}else{
			outltecode="            ";
			if(!isMini){
				if(isHeader){
					headerItem="*"+"القيمة   "+"              "+"الحبة     "+"الباكيت   "+"  الكمية"+(printoutlet==1?"":outltecode)+"     "+(printoutlet==1?outltecode:"")+"      الصنف"+"!";
				}else{
					headerItem="*"+"         "+"              "+"سعر       "+"سعر       "+"        "+(printoutlet==1?"":outltecode)+"     "+(printoutlet==1?outltecode:"")+"        رقم"+"!";
				}
			}else{
				if(isHeader){
					headerItem="*"+"الكمية  "+"           "+(printoutlet==1?"":outltecode)+"                           الصنف"+(printoutlet==1?outltecode:"")+"     الصنف"+"!";
				}else{
					headerItem="*"+"        "+"           "+(printoutlet==1?"":outltecode)+"                             وصف"+(printoutlet==1?outltecode:"")+"       رقم"+"!";
				}
			}
			
		}
		
		
		return headerItem;
	}
	
	public static String getArabicHeaderVal(String headerVal,String cases,String pcs,String qty,boolean isHeader){
		String arabicHeaderVal="";
		hashArabValues=new HashMap<String, String>();
		hashArabValues.put("SL#", "sl#");
		hashArabValues.put("ITEM#", "صنف");
		hashArabValues.put("DESCRIPTION", "وصف");
		hashArabValues.put("ARBDESCRIPTION", "            وصف");
		hashArabValues.put("Barcode", "الباركود");
		hashArabValues.put("OUTLET CODE", "منفذ");
		hashArabValues.put("UOM", ArabicTEXT.UPC);
		hashArabValues.put("DISCOUNT", ArabicTEXT.DISCOUNT);
		hashArabValues.put("GROSS AMOUNT","الإجمالي");
		hashArabValues.put("EXCISE TAX", ArabicTEXT.EXCISETAX);
		hashArabValues.put("VAT AMOUNT", ArabicTEXT.VATAMOUNT);
		hashArabValues.put("AMOUNT", ArabicTEXT.AMOUNT);
		hashArabValues.put("NET PRICE", ArabicTEXT.NetAmount);
		if(hashArabValues.containsKey(headerVal)){
			arabicHeaderVal=hashArabValues.get(headerVal);
		}
		else{
			if(headerVal.contains(qty)){
				arabicHeaderVal=ArabicTEXT.QTY;
			}else if(headerVal.contains(pcs)){
				arabicHeaderVal="سعر";
			}else if(headerVal.contains(cases)){
					arabicHeaderVal="سعر";
				} 
				else{
				arabicHeaderVal=headerVal;

			}
		}

		return  arabicHeaderVal;
	}
	public static String getArabicHeaderBottomVal(String headerVal,String cases,String pcs,String qty,boolean isHeader){
		String arabicHeaderBottomVal="";
		
		hashArabValues=new HashMap<String, String>();
		hashArabValues.put("SL#", "''");
		hashArabValues.put("ITEM#","الصنف");
		hashArabValues.put("DESCRIPTION", "الصنف");
		hashArabValues.put("ARBDESCRIPTION", "        العربية");
		hashArabValues.put("Barcode"," ");
		hashArabValues.put("OUTLET CODE", "رمز ");
		hashArabValues.put("UOM","    ");
		hashArabValues.put("DISCOUNT", " ");
		hashArabValues.put("GROSS AMOUNT"," ");
		hashArabValues.put("EXCISE TAX", " ");
		hashArabValues.put("VAT AMOUNT", " ");
		hashArabValues.put("AMOUNT", " ");
		hashArabValues.put("NET PRICE", " ");
		
		if(hashArabValues.containsKey(headerVal)){
			arabicHeaderBottomVal=hashArabValues.get(headerVal);
		}
		else{
			if(headerVal.contains(qty)){
				arabicHeaderBottomVal="";
			}else if(headerVal.contains(pcs)){
				arabicHeaderBottomVal="الحبة";
			}else if(headerVal.contains(cases)){
					arabicHeaderBottomVal="الباكيت";
				} else{
				arabicHeaderBottomVal=headerVal;

			}
			
		}
		return arabicHeaderBottomVal;
	}
}
