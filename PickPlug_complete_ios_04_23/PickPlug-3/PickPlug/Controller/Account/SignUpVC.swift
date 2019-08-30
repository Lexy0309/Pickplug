//
//  SignUpVC.swift
//  PickPlug
//
//  Created by abhinav on 25/06/18.
//  Copyright © 2018 abhinav. All rights reserved.
//

import UIKit
import FacebookLogin
import FBSDKLoginKit
import FacebookCore

class SignUpVC: UIViewController,ServiceApiResponseDelegate,FBSDKLoginButtonDelegate {
    
    @IBOutlet var txtUserName: AITextField!
    @IBOutlet var txtEmail: AITextField!
    @IBOutlet var txtPass: AITextField!
    @IBOutlet var txtConfirmPass: AITextField!
    @IBOutlet var btnCreateAcc: AIButton!
    @IBOutlet var lblLoginFb: AILabel!
    @IBOutlet var viewLoginFb: UIView!
    var facebookBtn = FBSDKLoginButton()
    var loginWith = ""
    var facebookToken = ""
    var dictFacebookData = NSDictionary.init()
    
    @IBOutlet weak var btnCheckbox: ISRadioButton!
    @IBOutlet var fbLoginWidthCons: NSLayoutConstraint!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
        Utility.setPlaceholderColor(textField: txtUserName, placeholderText: "Enter Full Name", color: Utility.STRING_TEXT_COLOR)
        Utility.setPlaceholderColor(textField: txtEmail, placeholderText: "Enter Email Address", color: Utility.STRING_TEXT_COLOR)
        Utility.setPlaceholderColor(textField: txtPass, placeholderText: "Password", color: Utility.STRING_TEXT_COLOR)
        Utility.setPlaceholderColor(textField: txtConfirmPass, placeholderText: "Confirm Password", color: Utility.STRING_TEXT_COLOR)
        // Utility.decorateButton(button: btnCreateAcc, borderColor: "", borderWidth: 0, cornerRadius: btnCreateAcc.frame.size.height / 2)
        //Utility.decorateView(view: viewLoginFb, borderColor: "", borderWidth: 0, cornerRadius: viewLoginFb.frame.size.height / 2)
        
        if ((FBSDKAccessToken.current()) != nil){
            print("Logged in !",FBSDKAccessToken.current().tokenString)
            //facebookToken = FBSDKAccessToken.current().tokenString
            self.logUserData()
        }else{
            print("Not Logged ")
        }
        facebookBtn = FBSDKLoginButton.init(type: .custom)
        facebookBtn.readPermissions = ["public_profile", "email"]
        facebookBtn.delegate = self
        FBSDKProfile.enableUpdates(onAccessTokenChange: true)
        
        if UIDevice.current.userInterfaceIdiom == .pad{
            fbLoginWidthCons.constant = UIScreen.main.bounds.width / 2
        }else{
            fbLoginWidthCons.constant = UIScreen.main.bounds.width - 40
        }
        
        btnCheckbox.multipleSelectionEnabled = true
        
    }
    func logUserData() {
        
        let parameter = ["fields":"email,first_name,last_name,picture,birthday,gender"]
        FBSDKGraphRequest.init(graphPath: "me", parameters: parameter).start { (connection, result, error) in
            if error != nil {
                print("error graph ",error?.localizedDescription as Any)
            } else {
                print("result",result!,"==== token ",FBSDKAccessToken.current().tokenString)
                self.dictFacebookData = result as! NSDictionary
                self.loginWith = "facebook"
                self.facebookToken = Utility.getStringSafely(strData: self.dictFacebookData.object(forKey: "id"))
                Utility.writeString(key: Utility.KEY_IS_LOGIN_FACEBOOK, value: "yes")
                connection?.start()
                self.SignUpUser()
                
            }
        }
        
    }
    func loginButton(_ loginButton: FBSDKLoginButton!, didCompleteWith result: FBSDKLoginManagerLoginResult!, error: Error!) {
        
        if ((error) == nil){
            print("Login Complete")
            logUserData()
        }else {
            print ("Voici l'erreur:"+error.localizedDescription)
        }
    }
    
    func loginButtonDidLogOut(_ loginButton: FBSDKLoginButton!) {
        let facebookLogin = FBSDKLoginManager()
        facebookLogin.logOut()
        print("User Logged Out")
    }
    override func viewDidAppear(_ animated: Bool) {
        
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    @IBAction func onClickBcak(_ sender: Any) {
        self.navigationController?.popViewController(animated: true)
    }
    
    
    @IBAction func onClickCreateAcc(_ sender: Any) {
        txtEmail.resignFirstResponder()
        txtPass.resignFirstResponder()
        txtConfirmPass.resignFirstResponder()
        txtUserName.resignFirstResponder()
        loginWith = "user"
        Utility.writeString(key: Utility.KEY_IS_LOGIN_FACEBOOK, value: "no")
        var errorCnt = 0
        var stringAlert = NSMutableString.init(string: "")
        if !Utility.isValidField(textField: txtUserName) {
            errorCnt  = errorCnt+1
            stringAlert = "Enter your full name"
        }else if !Utility.isValidField(textField: txtEmail) {
            errorCnt  = errorCnt+1
            stringAlert = "Enter email address"
        }else if !Utility.isValidEmail(email: txtEmail.text!) {
            errorCnt  = errorCnt+1
            stringAlert = "Enter valid email address"
        }else if !Utility.isValidField(textField: txtPass) {
            errorCnt  = errorCnt+1
            stringAlert = "Enter your password"
        }else if !Utility.isValidField(textField: txtConfirmPass) {
            errorCnt  = errorCnt+1
            stringAlert = "Enter your confirm password"
        }else if (txtPass.text != txtConfirmPass.text){
            errorCnt  = errorCnt+1
            stringAlert = "Password and confirm password does not match"
        }else if btnCheckbox.isSelected == false {
            errorCnt  = errorCnt+1
            stringAlert = "Please accept term and conditions"
        }
        if errorCnt > 0 {
            Utility.showSnackBar(message: stringAlert as String)
        }else{
            SignUpUser()
        }
    }
    func SignUpUser(){
        
        if Utility.isInternetAvailable() {
            Utility.showLoader()
            var parameter : [String:String]!
            var strUrl = ""
            if loginWith == "facebook"{
                let userName = Utility.getStringSafely(strData: dictFacebookData.object(forKey: "first_name")) + " " + Utility.getStringSafely(strData: dictFacebookData.object(forKey: "last_name"))
                let userEmail = Utility.getStringSafely(strData: dictFacebookData.object(forKey: "email"))
                let profilePic = Utility.getStringSafely(strData: (((dictFacebookData.object(forKey: "picture") as! NSDictionary).object(forKey: "data") as! NSDictionary).object(forKey: "url")))
                strUrl = Utility.LOGIN_WITH_FACEBOOK
                parameter = ["email":userEmail,"fullname":userName,"image":profilePic,"token":facebookToken,"birthday":"00-00-0000"]
                
                
            }else{
                strUrl = Utility.SIGNUP_USER
                parameter = ["fullname":txtUserName.text!,"email":txtEmail.text!,"password":txtPass.text!,]
                
                
            }
            Utility.sendRequest(forUrl: strUrl, parameters: parameter, requestMethod: AIHTTPMethod.post, others: [:], withDelegate: self)
        }else{
            Utility.showSnackBar(message: Utility.KEY_NO_INTERNET)
        }
    }
    
    func serviceResponseCallBack(response: Any) {
        Utility.hideLoader()
        print("Response ",response)
        var resultObj = NSDictionary.init()
        if loginWith == "facebook"{
            resultObj = (response as! NSDictionary).object(forKey: "Login") as! NSDictionary
        } else{
            resultObj = (response as! NSDictionary).object(forKey: "register") as! NSDictionary
        }
        if Utility.getStringSafely(strData: resultObj.object(forKey: "status")) == "success" {
            let modelObj = resultObj.object(forKey: "user") as! NSDictionary
            Utility.writeString(key: Utility.KEY_USER_ID, value: Utility.getStringSafely(strData: modelObj.object(forKey: "Id")))
            Utility.writeString(key: Utility.KEY_USER_NAME, value: Utility.getStringSafely(strData: modelObj.object(forKey: "FullName")))
            Utility.writeString(key: Utility.KEY_EMAIL, value: Utility.getStringSafely(strData: modelObj.object(forKey: "Email")))
            Utility.writeString(key: Utility.KEY_USER_IMAGE, value: Utility.getStringSafely(strData: modelObj.object(forKey: "Photo")))
            Utility.writeString(key: Utility.KEY_USER_TYPE, value: Utility.getStringSafely(strData: modelObj.object(forKey: "type")))
            let current_date = Date()
            Utility.writeAny(key: "last_updated", value: current_date)
            if loginWith != "facebook"{
                Utility.writeString(key: Utility.KEY_PASSWORD, value: txtPass.text!)
            }
            let storyboard: UIStoryboard = self.storyboard! //UIStoryboard(name: "Home", bundle: nil)
            let controller = storyboard.instantiateViewController(withIdentifier: "KYDrawerController") as! KYDrawerController
            let navController = UINavigationController.init(rootViewController: controller)
            navController.setNavigationBarHidden(true, animated: false)
            present(navController, animated: true, completion: nil)
        }else{
            Utility.showSnackBar(message: Utility.getStringSafely(strData: resultObj.object(forKey: "message")))
        }
    }
    
    @IBAction func onClickFacebook(_ sender: Any) {
        if btnCheckbox.isSelected == false {
            Utility.showSnackBar(message: "Please accept term and conditions")
        }else{
            self.facebookBtn.sendActions(for: .touchUpInside)
        }
        
        
    }
    @IBAction func onClickTerms(_ sender: Any) {
        let vc = self.storyboard?.instantiateViewController(withIdentifier: "Legal_VC") as! LegalVC
        self.navigationController?.pushViewController(vc, animated: true)
    }
    /*
     // MARK: - Navigation
     
     // In a storyboard-based application, you will often want to do a little preparation before navigation
     override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
     // Get the new view controller using segue.destinationViewController.
     // Pass the selected object to the new view controller.
     }
     */
    
}

