//
//  AppDelegate.swift
//  PickPlug
//
//  Created by abhinav on 25/06/18.
//  Copyright © 2018 abhinav. All rights reserved.
//

import UIKit
import IQKeyboardManagerSwift
import FacebookLogin
import FBSDKLoginKit
import UserNotifications
import FirebaseCore
import FirebaseMessaging
import FirebaseInstanceID
import StoreKit

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate,DismissAlertDelegate,ServiceApiResponseDelegate {

    var window: UIWindow?
    let gcmMessageIDKey = "gcm.message_id"
    var currentController : UIViewController!;
    var arrTab = NSMutableArray.init()
    var modelObj = NSDictionary.init()
    var products = [SKProduct]()
    var subscriptionList =  NSMutableArray.init()
    
    
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplicationLaunchOptionsKey: Any]?) -> Bool {
        // Override point for customization after application launch.
        IQKeyboardManager.shared.enable = true
        IQKeyboardManager.shared.shouldResignOnTouchOutside = true
        FBSDKApplicationDelegate.sharedInstance().application(application, didFinishLaunchingWithOptions: launchOptions)
        FirebaseApp.configure()
        
        // [START set_messaging_delegate]
        Messaging.messaging().delegate = self
        // [END set_messaging_delegate]
        // Register for remote notifications. This shows a permission dialog on first run, to
        // show the dialog at a more appropriate time move this registration accordingly.
        // [START register_for_notifications]
        if #available(iOS 10.0, *) {
            // For iOS 10 display notification (sent via APNS)
            UNUserNotificationCenter.current().delegate = self
            
            let authOptions: UNAuthorizationOptions = [.alert, .badge, .sound]
            UNUserNotificationCenter.current().requestAuthorization(
                options: authOptions,
                completionHandler: {_, _ in })
        } else {
            let settings: UIUserNotificationSettings =
                UIUserNotificationSettings(types: [.alert, .badge, .sound], categories: nil)
            application.registerUserNotificationSettings(settings)
        }
        
        
        
        reload()
        
        let val_startDate = UserDefaults.standard.object(forKey: "startDate")
        let val_setRate = UserDefaults.standard.object(forKey: "setRate")
        let calendar = Calendar.current
        if(val_startDate == nil){
            let current_date = Date()
            UserDefaults.standard.set(current_date, forKey: "startDate")
        }
        else{
            let today = Date()
            let differenceDate = calendar.dateComponents([Calendar.Component.year, .month, .day], from: val_startDate as! Date, to: today)
            if (differenceDate.day! >= 5 && val_setRate == nil){
                SKStoreReviewController.requestReview()
                UserDefaults.standard.set("success", forKey: "setRate")
            }
        }
        
        let user_name = Utility.readString(key: Utility.KEY_USER_NAME)
        let user_email = Utility.readString(key: Utility.KEY_EMAIL)
        if (user_name != nil && user_email != nil){
            
            let strUrl = Utility.GET_USER_APP_LOGS
            let timeInSeconds: TimeInterval = Date().timeIntervalSince1970
            let parameter = ["user_name":user_name as! String,"user_email":user_email as! String,"last_updated":String(format: "%d", Int(timeInSeconds))]
            Utility.sendRequest(forUrl: strUrl, parameters: parameter, requestMethod: AIHTTPMethod.post, others: [:], withDelegate: self)
            
        }
        
        //validateAppReceipt()
        
       // setupIAP();
        
        application.registerForRemoteNotifications()
        FBSDKApplicationDelegate.sharedInstance().application(application, didFinishLaunchingWithOptions: launchOptions)
        return true
    }
    
    func validateAppReceipt() {
        let recuptUrl = Bundle.main.appStoreReceiptURL
        do {
            print(recuptUrl?.absoluteString)
            
            let receipt: Data = try Data.init(contentsOf: recuptUrl!)
            let base64encodedReceipt = receipt.base64EncodedString()
            print(base64encodedReceipt)
            let requestDictionary = ["receipt-data":base64encodedReceipt,"password":"5450c610a1954ace8db37437cc7ba0c0"]
            guard JSONSerialization.isValidJSONObject(requestDictionary) else {  print("requestDictionary is not valid JSON");  return }
            do {
                let requestData = try JSONSerialization.data(withJSONObject: requestDictionary)
                let validationURLString = "https://sandbox.itunes.apple.com/verifyReceipt"  // this works but as noted above it's best to use your own trusted server
                guard let validationURL = URL(string: validationURLString) else { print("the validation url could not be created, unlikely error"); return }
                let session = URLSession(configuration: URLSessionConfiguration.default)
                var request = URLRequest(url: validationURL)
                request.httpMethod = "POST"
                request.cachePolicy = URLRequest.CachePolicy.reloadIgnoringCacheData
                let task = session.uploadTask(with: request, from: requestData) { (data, response, error) in
                    if let data = data , error == nil {
                        do {
                            print(data)
                            let appReceiptJSON = try JSONSerialization.jsonObject(with: data)
                            print("success. here is the json representation of the app receipt: \(appReceiptJSON)")
                            //self.getAppReceipt()
                        } catch let error as NSError {
                            print("json serialization failed with error: \(error)")
                        }
                    } else {
                        print("the upload task returned an error: \(String(describing: error))")
                    }
                }
                task.resume()
            } catch let error as NSError {
                print("json serialization failed with error: \(error)")
            }
        } catch {
            print("Unable to load data: \(error)")
        }
    }

    /*func setupIAP() {
        
        SwiftyStoreKit.completeTransactions(atomically: true) { purchases in
            
            for purchase in purchases {
                switch purchase.transaction.transactionState {
                case .purchased, .restored:
                    if purchase.needsFinishTransaction {
                        // Deliver content from server, then:
                        SwiftyStoreKit.finishTransaction(purchase.transaction)
                    }
                    print("\(purchase.transaction.transactionState.debugDescription): \(purchase.productId)")
                case .failed, .purchasing, .deferred:
                    break // do nothing
                }
            }
        }
    }*/
    func updateSubscription( _ products: [SKProduct]){
        let arrPurchasedProducts = NSMutableArray.init()
            for p in products {
                print("Found product: \(p.productIdentifier) \(p.localizedTitle) \(p.price.floatValue)")
                arrPurchasedProducts.add(p.productIdentifier)
            }
            let stringProductList = arrPurchasedProducts.componentsJoined(by:",")
            if Utility.isInternetAvailable() {
                Utility.showLoader()
                let strUrl = Utility.UPDATE_SUBSCRIPTION
    
                Utility.sendRequest(forUrl: strUrl , parameters: ["user":Utility.readString(key: Utility.KEY_USER_ID),"productId":stringProductList], requestMethod: AIHTTPMethod.post, others: [:], withDelegate: self)
            }else{
                Utility.showSnackBar(message: Utility.KEY_NO_INTERNET)
            }
        }
    func reload() {
        products = []
        PickPlugProducts.store.requestProducts{success, products in
            if success {                
                self.products = products!
            }
        }
        updateSubscription(self.products)
    }
    
    
    
    func application(_ app: UIApplication, open url: URL, options: [UIApplicationOpenURLOptionsKey : Any] = [:]) -> Bool {
        return FBSDKApplicationDelegate.sharedInstance().application(app, open: url, sourceApplication: options[UIApplicationOpenURLOptionsKey.sourceApplication] as! String, annotation: options[UIApplicationOpenURLOptionsKey.annotation])
    }
    
   
    
    func applicationWillResignActive(_ application: UIApplication) {
        // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
        // Use this method to pause ongoing tasks, disable timers, and invalidate graphics rendering callbacks. Games should use this method to pause the game.
    }

    func applicationDidEnterBackground(_ application: UIApplication) {
        // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
        // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
    }

    func applicationWillEnterForeground(_ application: UIApplication) {
        // Called as part of the transition from the background to the active state; here you can undo many of the changes made on entering the background.
    }

    func applicationDidBecomeActive(_ application: UIApplication) {
        // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
    }

    func applicationWillTerminate(_ application: UIApplication) {
        // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
    }
    func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable: Any]) {
        // If you are receiving a notification message while your app is in the background,
        // this callback will not be fired till the user taps on the notification launching the application.
        // TODO: Handle data of notification
        // With swizzling disabled you must let Messaging know about the message, for Analytics
        // Messaging.messaging().appDidReceiveMessage(userInfo)
        // Print message ID.
        if let messageID = userInfo[gcmMessageIDKey] {
            print("Message ID: \(messageID)")
        }
        
        print("Message Block 1")
        // Print full message.
        print(userInfo)
    }
    
    func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable: Any],
                     fetchCompletionHandler completionHandler: @escaping (UIBackgroundFetchResult) -> Void) {
        // If you are receiving a notification message while your app is in the background,
        // this callback will not be fired till the user taps on the notification launching the application.
        // TODO: Handle data of notification
        // With swizzling disabled you must let Messaging know about the message, for Analytics
        // Messaging.messaging().appDidReceiveMessage(userInfo)
        // Print message ID.
        if let messageID = userInfo[gcmMessageIDKey] {
            print("Message ID: \(messageID)")
        }
        
        print("Message Block 2")
        // Print full message.
        print(userInfo)
        
        completionHandler(UIBackgroundFetchResult.newData)
    }
    // [END receive_message]
    func application(_ application: UIApplication, didFailToRegisterForRemoteNotificationsWithError error: Error) {
        print("Unable to register for remote notifications: \(error.localizedDescription)")
    }
    
    // This function is added here only for debugging purposes, and can be removed if swizzling is enabled.
    // If swizzling is disabled then this function must be implemented so that the APNs token can be paired to
    // the FCM registration token.
    func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        print("APNs token retrieved: \(deviceToken)")
        
        // With swizzling disabled you must set the APNs token here.
        // Messaging.messaging().apnsToken = deviceToken
    }
    func onDismissAlert() {
        
    }
    
    func onReceiveNotification(userInfo:NSDictionary) {
        print("onReceiveNotification",userInfo);
        if currentController != nil {
            let messages = (userInfo["aps"] as! NSDictionary).object(forKey: "alert") as! NSDictionary
            Utility.showAlert(title: (messages["title"] as! String), message: (messages["body"] as! String), controller: currentController, delegate: self)
        }

        
    }
    func updateToken(){
        if Utility.isInternetAvailable() {
           // Utility.showLoader()
            var parameter : [String:String]!
            parameter = ["device":"ios","token":Utility.readString(key: Utility.Key_Firebase_Token)]
            
            Utility.sendRequest(forUrl: Utility.UPDATE_TOKEN, parameters: parameter, requestMethod: AIHTTPMethod.post, others: [:], withDelegate: self)
        }else{
            Utility.showSnackBar(message: Utility.KEY_NO_INTERNET)
        }
    }
    
    func serviceResponseCallBack(response: Any) {
        Utility.hideLoader()
        print("Response Token",response)
        
    }

}
extension AppDelegate : MessagingDelegate {
    // [START refresh_token]
    func messaging(_ messaging: Messaging, didReceiveRegistrationToken fcmToken: String) {
        print("Firebase registration token: \(fcmToken)")
        Utility.writeString(key: Utility.Key_Firebase_Token, value: fcmToken);
        if !Utility.readString(key: Utility.Key_Firebase_Token).isEmpty {
            updateToken()
        }
        
        // TODO: If necessary send token to application server.
        // Note: This callback is fired at each app startup and whenever a new token is generated.
    }
    // [END refresh_token]
    // [START ios_10_data_message]
    // Receive data messages on iOS 10+ directly from FCM (bypassing APNs) when the app is in the foreground.
    // To enable direct data messages, you can set Messaging.messaging().shouldEstablishDirectChannel to true.
    func messaging(_ messaging: Messaging, didReceive remoteMessage: MessagingRemoteMessage) {
        print("Received data message: \(remoteMessage.appData)")
        print("Message Block 3")
    }
}


// [START ios_10_message_handling]
@available(iOS 10, *)
extension AppDelegate : UNUserNotificationCenterDelegate {
    
    // Receive displayed notifications for iOS 10 devices.
    func userNotificationCenter(_ center: UNUserNotificationCenter,
                                willPresent notification: UNNotification,
                                withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void) {
        let userInfo = notification.request.content.userInfo
        
        // With swizzling disabled you must let Messaging know about the message, for Analytics
        // Messaging.messaging().appDidReceiveMessage(userInfo)
        // Print message ID.
        if let messageID = userInfo[gcmMessageIDKey] {
            print("Message ID: \(messageID)")
        }
        
        print("Message Block 4")
        // Print full message.
        print(userInfo)
        
        self.onReceiveNotification(userInfo: userInfo as NSDictionary)
        
        // Change this to your preferred presentation option
        completionHandler([])
    }
    
    func userNotificationCenter(_ center: UNUserNotificationCenter,
                                didReceive response: UNNotificationResponse,
                                withCompletionHandler completionHandler: @escaping () -> Void) {
        let userInfo = response.notification.request.content.userInfo
        // Print message ID.
        if let messageID = userInfo[gcmMessageIDKey] {
            print("Message ID: \(messageID)")
        }
        DispatchQueue.main.asyncAfter(deadline: .now() + .seconds(1), execute: {
            self.onReceiveNotification(userInfo: userInfo as NSDictionary)
        })
        
        //appComesFromBackgroundViaMessage = true
        //self.onReceiveNotification(userInfo: userInfo as NSDictionary)
        print("Message Block 5")
        
        // Print full message.
        print(userInfo)
        
        completionHandler()
    }
}

