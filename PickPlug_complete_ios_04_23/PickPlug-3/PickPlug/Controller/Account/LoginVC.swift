//
//  LoginVC.swift
//  PickPlug
//
//  Created by abhinav on 25/06/18.
//  Copyright © 2018 abhinav. All rights reserved.
//

import UIKit
import FacebookLogin
import FBSDKLoginKit
import FacebookCore

class LoginVC: UIViewController,ServiceApiResponseDelegate,FBSDKLoginButtonDelegate {
    
    @IBOutlet var txtEmail: AITextField!
    @IBOutlet var txtPass: AITextField!
    @IBOutlet var btnForgotPass: AIButton!
    @IBOutlet var btnLogin: AIButton!
    @IBOutlet var btnRegister: AIButton!
    @IBOutlet var lblLoginFb: AILabel!
    @IBOutlet var viewLoginFb: UIView!
    @IBOutlet var lblSigningYou: AILabel!
    @IBOutlet var lblTerms: AILabel!
    @IBOutlet var imgBGImage: UIImageView!
    var facebookBtn = FBSDKLoginButton.init()
    
    @IBOutlet var fbLoginWithCons: NSLayoutConstraint!
    @IBOutlet var loginSignUpViewWidthCons: NSLayoutConstraint!
    var loginWith = ""
    var facebookToken = ""
    var dictFacebookData = NSDictionary.init()
    
    @IBOutlet weak var btnCheckbox: ISRadioButton!
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
        
        Utility.setPlaceholderColor(textField: txtEmail, placeholderText: "Email Address", color: Utility.STRING_TEXT_COLOR)
        Utility.setPlaceholderColor(textField: txtPass, placeholderText: "Password", color: Utility.STRING_TEXT_COLOR)
        Utility.decorateButton(button: btnLogin, borderColor: "", borderWidth: 0, cornerRadius: btnLogin.frame.size.height / 2)
        Utility.decorateButton(button: btnRegister, borderColor: "", borderWidth: 0, cornerRadius: btnLogin.frame.size.height / 2)
        Utility.decorateView(view: viewLoginFb, borderColor: "", borderWidth: 0, cornerRadius: viewLoginFb.frame.size.height / 2)
        (UIApplication.shared.delegate as! AppDelegate).currentController = self
        
        // print("Access token",FBSDKAccessToken.current().tokenString)
        
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
            fbLoginWithCons.constant = UIScreen.main.bounds.width / 2
            loginSignUpViewWidthCons.constant  = UIScreen.main.bounds.width / 2
        }else{
            loginSignUpViewWidthCons.constant  = UIScreen.main.bounds.width - 40
            fbLoginWithCons.constant = UIScreen.main.bounds.width - 40
        }
        
        btnCheckbox.multipleSelectionEnabled = true
        
    }
    override func viewDidAppear(_ animated: Bool) {
        
    }
    
    
    override var preferredStatusBarStyle: UIStatusBarStyle{
        return .lightContent
    }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    @IBAction func onClickFacebook(_ sender: Any) {
        
        if btnCheckbox.isSelected == false {
            Utility.showSnackBar(message: "Please accept term and conditions")
        }else{
            self.facebookBtn.sendActions(for: .touchUpInside)
        }
        
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
                self.loginUser()
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
    
    @IBAction func onClickForgotPass(_ sender: Any) {
        let vc = self.storyboard?.instantiateViewController(withIdentifier: "ForgotPass_VC") as! ForgotPassVC
        self.navigationController?.pushViewController(vc, animated: true)
    }
    @IBAction func onClickBack(_ sender: Any) {
        
    }
    @IBAction func onClickLogin(_ sender: Any) {
        txtEmail.resignFirstResponder()
        txtPass.resignFirstResponder()
        
        loginWith = "user"
        Utility.writeString(key: Utility.KEY_IS_LOGIN_FACEBOOK, value: "no")
        var errorCnt = 0
        var stringAlert = NSMutableString.init(string: "")
        
        if !Utility.isValidField(textField: txtEmail) {
            errorCnt  = errorCnt+1
            stringAlert = "Enter email address"
        }else if !Utility.isValidEmail(email: txtEmail.text!) {
            errorCnt  = errorCnt+1
            stringAlert = "Enter valid email address"
        }else if !Utility.isValidField(textField: txtPass) {
            errorCnt  = errorCnt+1
            stringAlert = "Enter your password"
        }else if btnCheckbox.isSelected == false {
            errorCnt  = errorCnt+1
            stringAlert = "Please accept term and conditions"
        }
        
        if errorCnt > 0 {
            Utility.showSnackBar(message: stringAlert as String)
        }else{
            loginUser()
        }
    }
    
    @IBAction func onClickRegister(_ sender: Any) {
        let vc = self.storyboard?.instantiateViewController(withIdentifier: "SignUp_VC") as! SignUpVC
        self.navigationController?.pushViewController(vc, animated: true)
    }
    
    func loginUser(){
        
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
                strUrl = Utility.LOGIN_USER
                parameter = ["email":txtEmail.text!,"password":txtPass.text!]
                
            }
            
            
            Utility.sendRequest(forUrl: strUrl, parameters: parameter, requestMethod: AIHTTPMethod.post, others: [:], withDelegate: self)
        }else{
            Utility.showSnackBar(message: Utility.KEY_NO_INTERNET)
        }
    }
    
    func serviceResponseCallBack(response: Any) {
        Utility.hideLoader()
        print("Response ",response)
        let resultObj = (response as! NSDictionary).object(forKey: "Login") as! NSDictionary
        if Utility.getStringSafely(strData: resultObj.object(forKey: "status")) == "success" {
            let modelObj = resultObj.object(forKey: "user") as! NSDictionary
            Utility.writeString(key: Utility.KEY_USER_ID, value: Utility.getStringSafely(strData: modelObj.object(forKey: "Id")))
            Utility.writeString(key: Utility.KEY_USER_NAME, value: Utility.getStringSafely(strData: modelObj.object(forKey: "FullName")))
            Utility.writeString(key: Utility.KEY_EMAIL, value: Utility.getStringSafely(strData: modelObj.object(forKey: "Email")))
            let current_date = Date()
            Utility.writeAny(key: "last_updated", value: current_date)
            if loginWith != "facebook"{
                Utility.writeString(key: Utility.KEY_PASSWORD, value: txtPass.text!)
            }
            
            Utility.writeString(key: Utility.KEY_USER_IMAGE, value: Utility.getStringSafely(strData: modelObj.object(forKey: "Photo")))
            Utility.writeString(key: Utility.KEY_USER_TYPE, value: Utility.getStringSafely(strData: modelObj.object(forKey: "type"))) // Instagram
            txtEmail.text = ""
            txtPass.text = ""
            
            let storyboard: UIStoryboard = self.storyboard! //UIStoryboard(name: "Home", bundle: nil)
            let controller = storyboard.instantiateViewController(withIdentifier: "KYDrawerController") as! KYDrawerController
            let navController = UINavigationController.init(rootViewController: controller)
            navController.setNavigationBarHidden(true, animated: false)
            present(navController, animated: true, completion: nil)
        }else{
            if Utility.getStringSafely(strData: resultObj.object(forKey: "message")) == "username and password empty" || Utility.getStringSafely(strData: resultObj.object(forKey: "message")) == "Invalid Login details"{
                Utility.showSnackBar(message: "Invalid email or password")
            }
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
