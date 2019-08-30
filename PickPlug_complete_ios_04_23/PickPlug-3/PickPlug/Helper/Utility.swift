//
//  Utility.swift
//  soul.media
//
//  Created by Archive Infotech on 13/05/17.
//  Copyright © 2017 Abhinav. All rights reserved.
//

import Foundation
import UIKit
import MapKit

protocol DismissAlertDelegate {
    func onDismissAlert()
}
protocol UtilityLocationDelegate {
    func getAddress(placemark:CLPlacemark)
}


public enum AIHTTPMethod: String {
    case options = "OPTIONS"
    case get     = "GET"
    case head    = "HEAD"
    case post    = "POST"
    case put     = "PUT"
    case patch   = "PATCH"
    case delete  = "DELETE"
    case trace   = "TRACE"
    case connect = "CONNECT"
}

extension UIColor {
    
    convenience init(hex: String) {
        let scanner = Scanner(string: hex)
        scanner.scanLocation = 0
        
        var rgbValue: UInt64 = 0
        
        scanner.scanHexInt64(&rgbValue)
        
        let r = (rgbValue & 0xff0000) >> 16
        let g = (rgbValue & 0xff00) >> 8
        let b = rgbValue & 0xff
        
        self.init(
            red: CGFloat(r) / 0xff,
            green: CGFloat(g) / 0xff,
            blue: CGFloat(b) / 0xff, alpha: 1
        )
    }
}

class Utility {
    
    static var BASE_URL                     =       "http://propicksapp.com/app/api/";
    static var LOGIN_USER                   =     BASE_URL + "signin.php";
    static var SIGNUP_USER                  =     BASE_URL + "register.php";
    static var FORGOT_PASSWORD              =     BASE_URL + "forgetpassword.php";
    static var UPDATE_USER_PROFILE          =     BASE_URL + "update_profile.php";
    static var CHANGE_PASSWORD              =     BASE_URL + "update_password.php";
    static var GET_SPORTS                   =     BASE_URL + "getAllSports.php?user_id=";
    static var GET_NHL_SPORTS               =     BASE_URL + "getPicksDetails.php?SportId=";
    static var GET_SUBSCRIPTION             =     BASE_URL + "getSubscriptions.php";
    static var RESTORE_SUBSCRIPTION         =     BASE_URL + "restoresubscription.php";
    static var LOGIN_WITH_FACEBOOK          =     BASE_URL + "instagram-login.php";
    static var UPDATE_TOKEN                 =     BASE_URL + "addandroidtoken.php";
    static var GET_NOTIFICATION             =     BASE_URL + "getNotification.php";
    static var ADD_SUBSCRIPTION             =     BASE_URL + "addsubscription.php";
    static var GET_FREEPICKS                =     BASE_URL + "getFreePickList.php";
    static var GET_PICK_BY_ID               =     BASE_URL + "getPicksBySportId.php";
    static var GET_USER_SUBSCRIPTION        =     BASE_URL + "getUserSubscription.php";
    
    static var GET_USER_APP_LOGS            =     BASE_URL + "getUserAppLog.php";
    static var UPDATE_SUBSCRIPTION          =     BASE_URL + "updateSubscription_android.php";
    static var LEGAL_URL                    =     "http://propicksapp.com/app/legal.html";
    static var PRIVACY_URL                  =     "http://propicksapp.com/app/privacypolicy.html";
    
    static var RATE_REVIEW_LINK             =     "https://itunes.apple.com/us/app/pick-plug/id1406824115?ls=1&mt=8";
    
    
    static var CURRENCY = " SR";
    
    //static var APP_COLOR :           UIColor = UIColor(red: 42 / 255, green: 44 / 255, blue: 45 / 255, alpha: 1.0)
   // static var STRING_TEXT_COLOR :UIColor   =   UIColor(red: 184 / 255, green: 184 / 255, blue: 184 / 255, alpha: 1.0)
    
   
    
    static var  KEY_NO_INTERNET              =   "Kindly check your Connectivity"
    static var  KEY_EXIT                     =   "Exit"
    static var  KEY_TRY_AGAIN                =   "Try Again"
    static var  KEY_UNABLE_TO_PROCESS        =   "Unable to process your request"
    static var  KEY_NO_RECORD_FOUND          =   "No record found"
    static var  KEY_SUCCESS                  =   "Success!"
    static var  KEY_ALERT                    =   "Alert!"
    static var  KEY_NO                       =   "No"
    static var  KEY_YES                      =   "Yes"
    static var  KEY_HELLO                    =   "Hello "
    
    static var  Key_Firebase_Token           =   "Key_Firebase_Token"
    
   
    static var  KEY_LANGUAGE                 =   "KEY_LANGUAGE"
    static var  KEY_USER_ID                  =   "KEY_USER_ID"
    static var  KEY_USER_NAME                =   "KEY_USER_NAME"
    static var  KEY_PASSWORD                 =   "KEY_PASSWORD"
    static var  KEY_EMAIL                    =   "KEY_EMAIL"
    static var  KEY_MOBILE                   =   "KEY_MOBILE"
    static var  KEY_IS_VERIFY                =   "KEY_IS_VERIFY"
    static var  KEY_USER_IMAGE               =   "KEY_USER_IMAGE"
    static var  KEY_LOGOUT                   =   "KEY_LOGOUT"
    static var  KEY_USER_TYPE                =   "KEY_USER_TYPE"
    static var  KEY_IS_LOGIN_FACEBOOK        =   "KEY_IS_LOGIN_FACEBOOK"
    static var  KEY_IS_TYPE_PICKS            =   "KEY_IS_TYPE_PICKS"
    static var  KEY_IS_SUBSCRIPTION          =   "KEY_IS_SUBSCRIPTION"
    static var  KEY_TRANSACTION_IDENTIFIER   =   "KEY_TRANSACTION_IDENTIFIER"
    static var  KEY_IS_PURCHASED             =   "KEY_IS_PURCHASED"
    static var  KEY_PICKS_SPORT_ID           =   "KEY_PICKS_SPORT_ID"
    static var  KEY_PICKS_ICON_URL           =   "KEY_PICKS_ICON_URL"
    static var  KEY_SIDE_MENU_OPEN           =   "KEY_SIDE_MENU_OPEN"
    
    
    static var  STRING_TEXT_COLOR            =   "#ffffff"
    static var  STRING_TEXT_COLOR_GREY       =   "#676767" //71AD6D green color
    static var  USER_PLACEHOLDER             =   "img_user_default.png"
    
   // E0E761 Yellow color
   class func randomString(length: Int) -> String {
        
        let letters : NSString = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        let len = UInt32(letters.length)
        
        var randomString = ""
        
        for _ in 0 ..< length {
            let rand = arc4random_uniform(len)
            var nextChar = letters.character(at: Int(rand))
            randomString += NSString(characters: &nextChar, length: 1) as String
        }
        
        return randomString
    }
    
    
    
    class func encodeUrl(url:String) ->String{
        return url.addingPercentEncoding(withAllowedCharacters: .urlHostAllowed)!
    }
    
    class func showLoader(){
        if (NVActivityIndicatorPresenter.sharedInstance.isAnimating == false) {
            NVActivityIndicatorPresenter.sharedInstance.startAnimating(ActivityData.init(size: CGSize(width: 30, height: 30), message: "Loading...",  type: NVActivityIndicatorType.ballPulse))
        }
    }
    class func hideLoader(){
        if (NVActivityIndicatorPresenter.sharedInstance.isAnimating == true) {
            NVActivityIndicatorPresenter.sharedInstance.stopAnimating()
        }
    }
    
    class func changeDateFormat(date:String)->String{
        
        let inputFormatter = DateFormatter();
        inputFormatter.dateFormat = "yyyy-mm-dd";
        let showDate = inputFormatter.date(from: date);
        inputFormatter.dateFormat = "dd-mm-yy";
        let resultString = inputFormatter.string(from: showDate!);
        return resultString;
        
    }
    
    class func getCurrentYear()->Int{
        let date = Date()
        let calendar = Calendar.current
        let year = calendar.component(.year, from: date)
        return year;
    }
    
    class func getMonthsDayCount(month:Int)->Int{
        
        let date = Date()
        let calendar = Calendar.current
        let year = calendar.component(.year, from: date)
        
        let dateComponents = DateComponents(year: year, month: month)
        let dateComponent = calendar.date(from: dateComponents)!
        
        let range = calendar.range(of: .day, in: .month, for: dateComponent)!
        let numDays = range.count
        
        return numDays;
        
    }
    
    
    class func getCurrentMonth()->Int{
        let date = Date()
        let calendar = Calendar.current
        let month = calendar.component(.month, from: date)
        return month;
    }
    
    
    class func getCurrentDate(){
        
        let date = NSDate()
        // *** create calendar object ***
        var calendar = NSCalendar.current
        
        // *** Get components using current Local & Timezone ***
        print(calendar.dateComponents([.year, .month, .day, .hour, .minute], from: date as Date))
        
        // *** define calendar components to use as well Timezone to UTC ***
        let unitFlags = Set<Calendar.Component>([.hour, .year, .minute])
        calendar.timeZone = TimeZone(identifier: "UTC")!
        
        // *** Get All components from date ***
        let components = calendar.dateComponents(unitFlags, from: date as Date)
        print("All Components : \(components)")
        
        // *** Get Individual components from date ***
        let hour = calendar.component(.hour, from: date as Date)
        let minutes = calendar.component(.minute, from: date as Date)
        let seconds = calendar.component(.second, from: date as Date)
        
        print("\(hour):\(minutes):\(seconds)")
        
        
    }
    
    class func isInternetAvailable() -> Bool{
        let reachable = Reachability();
        Utility.updateOnlineStatus();
        return reachable?.isReachable==true ? true :false
    }
    
    
    
    class func updateOnlineStatus(){
        let reachable = Reachability();
        if(reachable?.isReachable != true){
        }
    }
    
    
   
    class func isWifiAvaliable()-> Bool{
        let reachable = Reachability()
        return (reachable?.isReachableViaWiFi)!;
    }
    
    
    class func showSnackBar(message:String){
        let snackBar = TTGSnackbar.init(message: message, duration: .middle);
        snackBar.messageTextAlign = .center
        snackBar.show()
    }
    
    
    
    
    
    class func showAlert(title:String, message:String,controller:UIViewController, delegate:DismissAlertDelegate){
        let alert = UIAlertController(title: title, message: message, preferredStyle:.alert)
        alert.addAction(UIAlertAction(title: "Ok", style: .cancel, handler: { action in delegate.onDismissAlert()}))
        controller.present(alert, animated: true, completion: nil)
    }
    
    
    class func isValidField(textField:UITextField) -> Bool{
        if (textField.text?.trimmingCharacters(in: CharacterSet.whitespacesAndNewlines).count==0){
            return false;
        }else{
            return true
        }
    }
    
    class func isValidEmail(email:String) -> Bool{
        let emailRegex = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}"
        return NSPredicate(format: "SELF MATCHES %@", emailRegex).evaluate(with: email)
    }
    
    class func writeString(key:String,value:String){
        UserDefaults.standard.set(value, forKey: key);
        UserDefaults.standard.synchronize();
    }
    
    class func writeAny(key:String,value:Any){
        UserDefaults.standard.set(value, forKey: key);
        UserDefaults.standard.synchronize();
    }
    
    class func readString(key:String) -> String{
        if (UserDefaults.standard.string(forKey: key)==nil) {
            return ""
        }else{
            return UserDefaults.standard.string(forKey: key)!
        }
    }
    
    class func readAny(key:String) -> Any?{
       return UserDefaults.standard.object(forKey: key)
    }
    
    class func clearUserDefaults(){
        Utility.writeString(key: Utility.KEY_USER_ID, value: "")
        Utility.writeString(key: Utility.KEY_USER_NAME, value: "")
        Utility.writeString(key: Utility.KEY_EMAIL, value: "")
        Utility.writeString(key: Utility.KEY_PASSWORD, value: "")
        Utility.writeString(key: Utility.KEY_USER_IMAGE, value: "")
       /* for key in UserDefaults.standard.dictionaryRepresentation().keys {
            UserDefaults.standard.removeObject(forKey: key)
        }*/
    }
    
    class func getScreenWidth() -> CGFloat {
        return UIScreen.main.bounds.size.width
    }
    
    class func getScreenHeight() -> CGFloat {
        return UIScreen.main.bounds.size.height
    }
    
    class func getStringSafely(strData : Any?) -> String {
        
        if let safeString = strData as? String {
            return safeString
        }else if let safeString = strData as? Int {
            return String(safeString)
        }else if let safeString = strData as? Double {
            return String(safeString)
        }else{
            return ""
        }
    }
    
    class func getIntSafely(strData : Any) -> Int {
        if let safeString = strData as? Int {
            return safeString
        }else{
            return 0
        }
    }
    
    class func getDoubleSafely(strData : Any) -> Double {
        if let safeString = strData as? Double {
            return safeString
        }else{
            return 0
        }
    }
    
    class func getBoolSafely(strData : Any) -> Bool {
        if let safeString = strData as? Bool {
            return safeString
        }else{
            return false
        }
    }
    class func textFieldPadding(textField:UITextField,padding:Int){
        let paddingView : UIView = (UIView.init(frame: CGRect(x: 0, y: 0, width: padding, height: 20)))
        textField.leftView = paddingView
        textField.leftViewMode = UITextFieldViewMode.always
    }
    class func hexStringToUIColor (hex:String) -> UIColor {
        var cString:String = hex.trimmingCharacters(in: .whitespacesAndNewlines).uppercased()
        
        if (cString.hasPrefix("#")) {
            cString.remove(at: cString.startIndex)
        }
        
        if ((cString.count) != 6) {
            return UIColor.gray
        }
        
        var rgbValue:UInt32 = 0
        Scanner(string: cString).scanHexInt32(&rgbValue)
        
        return UIColor(
            red: CGFloat((rgbValue & 0xFF0000) >> 16) / 255.0,
            green: CGFloat((rgbValue & 0x00FF00) >> 8) / 255.0,
            blue: CGFloat(rgbValue & 0x0000FF) / 255.0,
            alpha: CGFloat(1.0)
        )
    }
    
    class func estimatedHeightOfLabel(text: String,width:CGFloat,font:UIFont) -> CGFloat {
        
        let size = CGSize(width: width, height: 1000)
        
        let options = NSStringDrawingOptions.usesFontLeading.union(.usesLineFragmentOrigin)
        
        let attributes = [NSAttributedStringKey.font: font]
        
        let rectangleHeight = String(text).boundingRect(with: size, options: options, attributes: attributes, context: nil).height
        
        return rectangleHeight
    }
    
    
    class func setPlaceholderColor(textField: UITextField, placeholderText: String, color:String) {
        textField.attributedPlaceholder = NSAttributedString(string: placeholderText, attributes: [NSAttributedStringKey.foregroundColor: Utility.hexStringToUIColor(hex: color)])
    }
    class func decorateLabel(label:UILabel,borderColor:String,borderWidth:CGFloat,cornerRadius:CGFloat){
        
        label.layer.cornerRadius = cornerRadius
        label.layer.borderColor = Utility.hexStringToUIColor(hex: borderColor).cgColor
        label.layer.borderWidth = borderWidth
    }
    class func decorateView(view:UIView,borderColor:String,borderWidth:CGFloat,cornerRadius:CGFloat){
        
        view.layer.cornerRadius = cornerRadius
        view.layer.borderColor = Utility.hexStringToUIColor(hex: borderColor).cgColor
        view.layer.borderWidth = borderWidth
    }
    class func decorateTextField(txtField:UITextField,borderColor:String,borderWidth:CGFloat,cornerRadius:CGFloat, Padding:Int,placeholderText:String,placeholderColor:String){
        
        txtField.layer.cornerRadius = cornerRadius
        txtField.layer.borderColor = Utility.hexStringToUIColor(hex: borderColor).cgColor
        txtField.layer.borderWidth = borderWidth
        Utility.textFieldPadding(textField: txtField, padding: Padding)
        Utility.setPlaceholderColor(textField: txtField, placeholderText: placeholderText, color: placeholderColor )
    }
    class func decorateButton(button:UIButton,borderColor:String,borderWidth:CGFloat,cornerRadius:CGFloat){
        button.layer.cornerRadius = cornerRadius
        button.layer.borderColor = Utility.hexStringToUIColor(hex: borderColor).cgColor
        button.layer.borderWidth = borderWidth
    }
    
    class func sendRequest(forUrl: String,parameters:[String: String],requestMethod : AIHTTPMethod,others:[String: AnyObject],withDelegate:ServiceApiResponseDelegate){
        print("API URL",forUrl);
        print("request parametere",parameters);
        AIWebService.init().sendRequest(forUrl: forUrl, parameters: parameters, requestMethod: requestMethod, others: others, withDelegate: withDelegate)
        
    }
    class func sendRequestMultiPart(forUrl: String,parameters:[String: String],requestMethod : AIHTTPMethod,imgParameter:String,imgFileName:String,imageFile:UIImage,withDelegate:ServiceApiResponseDelegate){
        print("API URL",forUrl);
        print("request parametere",parameters);
        AIWebService.init().sendRequestMultipart(forUrl: forUrl, parameters: parameters, requestMethod: requestMethod, imgParameter: imgParameter,imgFileName: imgFileName, imageFile: imageFile, withDelegate: withDelegate)
        
    }
    
    class func cancelAllReq() {
       AIWebService.init().cancelReq()
    }
    class func getLocation(strAddress:String, delegte:UtilityLocationDelegate){
        let geocoder = CLGeocoder()
        geocoder.geocodeAddressString(strAddress) { (placemarks, error) in
            if (error != nil){
                print("Reverse geocoder failed with error" + (error?.localizedDescription)!)
                return
            }
            if (placemarks?.count)! > 0 as Int{
                let pm = placemarks![0] as CLPlacemark
                delegte.getAddress(placemark: pm)
            }else{
                print("Problem with the data received from geocoder")
            }
        }
        
    }
    class func formattedDateFromString(dateString: String, withFormat format: String ,enterFormat: String) -> String? {
    
        let inputFormatter = DateFormatter()
        inputFormatter.dateFormat = enterFormat
        if let date = inputFormatter.date(from: dateString) {
            let outputFormatter = DateFormatter()
            outputFormatter.dateFormat = format
            return outputFormatter.string(from: date)
        }
        return nil
    }
     
}


