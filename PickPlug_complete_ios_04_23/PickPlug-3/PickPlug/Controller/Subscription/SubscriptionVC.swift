//
//  SubscriptionVC.swift
//  PickPlug
//
//  Created by abhinav on 29/06/18.
//  Copyright © 2018 abhinav. All rights reserved.
//

import UIKit
import StoreKit

extension UIView {
    func dropShadowSubscription(scale: Bool = true) {
        layer.masksToBounds = false
        layer.shadowOpacity = 0.8
        layer.shadowOffset = CGSize.zero
        layer.shadowRadius = 15
        layer.cornerRadius = 10
        layer.shadowColor = Utility.hexStringToUIColor(hex: "#AAAAAA").cgColor
    }
}

let priceFormatter: NumberFormatter = {
    let formatter = NumberFormatter()
    formatter.formatterBehavior = .behavior10_4
    formatter.numberStyle = .currency
    return formatter
}()

/*enum IAPHandlerAlertType{
    case disabled
    case restored
    case purchased
    
    func message() -> String{
        switch self {
        case .disabled: return "Purchases are disabled in your device!"
        case .restored: return "You've successfully restored your purchase!"
        case .purchased: return "You've successfully bought this purchase!"
        }
    }
}*/
class SubscriptionVC: UIViewController,HeaderViewBackDelegate,UITableViewDelegate,UITableViewDataSource,ServiceApiResponseDelegate,DismissAlertDelegate {

    @IBOutlet var headerView: HeaderView!
    @IBOutlet var viewTitle: UIView!
    @IBOutlet var lblTitle: AILabel!
    @IBOutlet var dataTableView: UITableView!
    var arrData = NSMutableArray.init()
    var callingFrom = ""
    var arrAmount = ["$999","$139","$499","$499","$499","$499","$499","$499","$499"]
    @IBOutlet var imgBackTrailingCons: NSLayoutConstraint!
    @IBOutlet var imgBackLeadingCons: NSLayoutConstraint!
    @IBOutlet var tableTopCons: NSLayoutConstraint!
    @IBOutlet var topHeadingViewHeightCons: NSLayoutConstraint!
    @IBOutlet var imgBackHeightCons: NSLayoutConstraint!
    @IBOutlet var imgBackImage: UIImageView!
    var products = [SKProduct]()
    var arrUserSubscription = NSMutableArray.init()
    var arrAllPurchaeSub =  NSMutableArray.init()
    var isAnyPurchaseYearlyOrMonthly : Bool =  false
    var idOfYearlyOrMonthlySubscription : String = ""
    var allSubscription = NSMutableArray.init()
    var arrPurchasedProducts = NSMutableArray.init()
    var strMessage : String = ""
    
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        headerView.backDelegate = self
        headerView.viewMenu.isHidden = true
        headerView.viewNotify.isHidden = true
        headerView.viewBack.isHidden = false
        headerView.btnRestore.isHidden = false
        headerView.btnRestore.addTarget(self, action: #selector(onClickRestore(sender:)), for: .touchUpInside)
        headerView.lblHeader.text = "Subscriptions"
        dataTableView.delegate = self
        dataTableView.dataSource = self
        dataTableView.estimatedRowHeight = 75
        dataTableView.rowHeight  = UITableViewAutomaticDimension
        dataTableView.tableFooterView = UIView.init()
        
        NotificationCenter.default.addObserver(self, selector: #selector(SubscriptionVC.handlePurchaseNotification(_:)),
                                               name: NSNotification.Name(rawValue: IAPHelper.IAPHelperPurchaseNotification),
                                               object: nil)
        
        NotificationCenter.default.addObserver(self, selector: #selector(SubscriptionVC.handleRestoreNotification(_:)),
                                               name: NSNotification.Name(rawValue: IAPHelper.IAPHelperRestoreNotification),
                                               object: nil)
        
        if UIDevice.current.userInterfaceIdiom == .pad{
            imgBackTrailingCons.constant = -70
            imgBackLeadingCons.constant = -70
            //imgBackHeightCons.constant = 90
            //topHeadingViewHeightCons.constant = 90
            tableTopCons.constant = 20
            viewTitle.dropShadowSubscription()
            imgBackImage.clipsToBounds = true
        }else{
            imgBackImage.clipsToBounds = false
            topHeadingViewHeightCons.constant = 70
            tableTopCons.constant = 18
            imgBackTrailingCons.constant = -25
            imgBackLeadingCons.constant = -25
        }
        
    }
    
    @objc func handleRestoreNotification(_ notification: Notification) {
        strMessage = "restore"
        guard let productID = notification.object as? String else { return }
        if arrPurchasedProducts.contains(productID) == false {
            arrPurchasedProducts.add(productID)
            Utility.writeString(key: Utility.KEY_IS_PURCHASED, value: "yes")
            for i in 0..<arrData.count{
                let modelObj = arrData.object(at: i) as! NSMutableDictionary
                let product_id =  Utility.getStringSafely(strData: modelObj.object(forKey: "ProductId"))
                if product_id == productID {
                    modelObj.setObject("true", forKey: "purchase_status" as NSCopying)
                    if Utility.isInternetAvailable() {
                        Utility.showLoader()
                        let strUrl = Utility.ADD_SUBSCRIPTION
                        let subsriptionRes =  AddSubscriptionResponse.init()
                        subsriptionRes.controller =  self
                        Utility.sendRequest(forUrl: strUrl , parameters: ["user":Utility.readString(key: Utility.KEY_USER_ID),"productId":productID,"transaction_id":Utility.readString(key: Utility.KEY_TRANSACTION_IDENTIFIER + productID)], requestMethod: AIHTTPMethod.post, others: [:], withDelegate: subsriptionRes)
                    }else{
                        Utility.showSnackBar(message: Utility.KEY_NO_INTERNET)
                    }
                    dataTableView.reloadRows(at: [IndexPath(row: i, section: 0)], with: .fade)
                    break
                }
            }
        }
    }
    

    @objc func handlePurchaseNotification(_ notification: Notification) {
        strMessage = "purchase"
        guard let productID = notification.object as? String else { return }
        if arrPurchasedProducts.contains(productID) == false {
            arrPurchasedProducts.add(productID)
            Utility.writeString(key: Utility.KEY_IS_PURCHASED, value: "yes")
            for i in 0..<arrData.count{
                let modelObj = arrData.object(at: i) as! NSMutableDictionary
                let product_id =  Utility.getStringSafely(strData: modelObj.object(forKey: "ProductId"))
                if product_id == productID {
                    modelObj.setObject("true", forKey: "purchase_status" as NSCopying)
                    if Utility.isInternetAvailable() {
                        Utility.showLoader()
                        let strUrl = Utility.ADD_SUBSCRIPTION
                        let subsriptionRes =  AddSubscriptionResponse.init()
                        subsriptionRes.controller =  self
                        Utility.sendRequest(forUrl: strUrl , parameters: ["user":Utility.readString(key: Utility.KEY_USER_ID),"productId":productID,"transaction_id":Utility.readString(key: Utility.KEY_TRANSACTION_IDENTIFIER + productID)], requestMethod: AIHTTPMethod.post, others: [:], withDelegate: subsriptionRes)
                    }else{
                        Utility.showSnackBar(message: Utility.KEY_NO_INTERNET)
                    }
                    dataTableView.reloadRows(at: [IndexPath(row: i, section: 0)], with: .fade)
                    break
                }
            }
        }
    }
    
    class AddSubscriptionResponse:ServiceApiResponseDelegate{
        var controller : SubscriptionVC!
        func serviceResponseCallBack(response: Any) {
            Utility.hideLoader()
            print("Response ",response)
            let resultObj = (response as! NSDictionary).object(forKey: "Results") as! NSDictionary
            if resultObj.object(forKey: "User_Subscriptions") != nil {
                if (resultObj.object(forKey: "User_Subscriptions") as! NSArray).count > 0 {
                    controller.arrUserSubscription  = (resultObj.object(forKey: "User_Subscriptions") as! NSArray).mutableCopy() as! NSMutableArray
                    controller.arrAllPurchaeSub    =  (controller.arrUserSubscription.value(forKey: "subscription_id") as! NSArray).mutableCopy() as! NSMutableArray
                    controller.updateData()
                    if controller.strMessage == "restore" {
                       
                    }else{
                        Utility.showAlert(title: "Thank you for your purchase!", message: "If you have any questions or concerns, please email: picks@pickplug.com", controller: controller, delegate: controller)
                    }
                    
                }
            }
            
        }
    }
    
    func onDismissAlert() {
        
    }

    func updateData() {
        arrData = NSMutableArray.init()
        
        for i in 0..<allSubscription.count{
            let modelObj = NSMutableDictionary.init(dictionary: allSubscription.object(at: i) as! NSDictionary)
            modelObj.setObject("false", forKey: "purchase_status" as NSCopying)
            
            if arrAllPurchaeSub.contains(Utility.getStringSafely(strData: modelObj.object(forKey: "Id"))) {
                modelObj.setObject("true", forKey: "purchase_status" as NSCopying)
                if Utility.getStringSafely(strData: modelObj.object(forKey: "Sport")) == "0" {
                    isAnyPurchaseYearlyOrMonthly =  true
                    idOfYearlyOrMonthlySubscription = Utility.getStringSafely(strData: modelObj.object(forKey: "Id"))
                }
            }
            let productID =  Utility.getStringSafely(strData: modelObj.object(forKey: "ProductId"))
            for (_, product) in products.enumerated() {
                if product.productIdentifier == productID {
                    if IAPHelper.canMakePayments() {
                        priceFormatter.locale = product.priceLocale
                        modelObj.setObject(priceFormatter.string(from: product.price)!, forKey: "price" as NSCopying)
                        arrData.add(modelObj)
                    }
                }
            }
        }
        self.dataTableView.reloadData()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func viewDidAppear(_ animated: Bool) {
        if arrData.count ==  0 {
            let appDelegate  = UIApplication.shared.delegate as! AppDelegate
            if appDelegate.products.count > 0 {
                products =  appDelegate.products
                getSubscribeDetails();
            }else{
                Utility.showSnackBar(message: "Something went wrong. Please try after some time")
            }
        }
        
        
    }
    func getSubscribeDetails(){
        if Utility.isInternetAvailable() {
            Utility.showLoader()
            var strUrl = ""
            if callingFrom == "restore"{
                strUrl = Utility.RESTORE_SUBSCRIPTION
            }else{
                strUrl = Utility.GET_SUBSCRIPTION
            }
            Utility.sendRequest(forUrl: strUrl , parameters: ["user":Utility.readString(key: Utility.KEY_USER_ID)], requestMethod: AIHTTPMethod.post, others: [:], withDelegate: self)
        }else{
            Utility.showSnackBar(message: Utility.KEY_NO_INTERNET)
        }
    }
    
    func serviceResponseCallBack(response: Any) {
        
        print("Response ",response)
        if callingFrom == "restore"{
            Utility.showSnackBar(message: Utility.getStringSafely(strData: Utility.getStringSafely(strData: (response as! NSDictionary).object(forKey: "Message"))))
        }else{
            isAnyPurchaseYearlyOrMonthly =  false
            idOfYearlyOrMonthlySubscription = ""
            
            let resultObj = (response as! NSDictionary).object(forKey: "Results") as! NSDictionary
            if  resultObj.object(forKey: "User_Subscriptions") is NSArray {
                if (resultObj.object(forKey: "User_Subscriptions") as! NSArray).count > 0 {
                    arrUserSubscription  = (resultObj.object(forKey: "User_Subscriptions") as! NSArray).mutableCopy() as! NSMutableArray
                    arrAllPurchaeSub    =  (arrUserSubscription.value(forKey: "subscription_id") as! NSArray).mutableCopy() as! NSMutableArray
                }
            }
            
            if (resultObj.object(forKey: "Subscriptions") as! NSArray).count > 0 {
                print("Product Loaded from itunes %@ ",products)
                allSubscription = (resultObj.object(forKey: "Subscriptions") as! NSArray).mutableCopy() as! NSMutableArray
            }
            updateData()
        }
        
        Utility.hideLoader()
    }
    func onClickBack(sender: Any) {
        self.navigationController?.popViewController(animated: true)
    }
    @objc func onClickRestore(sender: Any) {
       PickPlugProducts.store.restorePurchases()
    }
    
    override var preferredStatusBarStyle: UIStatusBarStyle{
        return .lightContent
    }
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return arrData.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cellIdentifier = "SubscriptionCell"
        var cell:SubscriptionCell! = dataTableView.dequeueReusableCell(withIdentifier: cellIdentifier) as? SubscriptionCell
        let modelObj = arrData.object(at: indexPath.row) as! NSMutableDictionary
        if cell == nil{
            let nib:NSArray = Bundle.main.loadNibNamed(cellIdentifier, owner: self, options: nil)! as NSArray
            cell = (nib.object(at: 0) as? SubscriptionCell)!
        }
        
        
        // This check is required because android app already publish and android app contains check with subscription name
        
        if Utility.getStringSafely(strData: modelObj.object(forKey: "Id")) == "23" {
            cell.lblTitle.text = "All Sports Yearly - Subscription"
        }else if Utility.getStringSafely(strData: modelObj.object(forKey: "Id")) == "24" {
            cell.lblTitle.text = "All Sports Monthly - Subscription"
        }else{
            cell.lblTitle.text = Utility.getStringSafely(strData: modelObj.object(forKey: "Name"))
        }
        
        cell.viewAlreadyBuy.isHidden = true
        cell.viewBuy.isHidden = false
        cell.btnBuy.tag = indexPath.row
        cell.btnBuy.addTarget(self, action: #selector(onClickBuy(sender:)), for: .touchUpInside)
        cell.lblBuy.text = "Buy\n" + Utility.getStringSafely(strData: modelObj.object(forKey: "price"))
        cell.lblAlreadyBuy.text = "" + Utility.getStringSafely(strData: modelObj.object(forKey: "price"))
        
        if isAnyPurchaseYearlyOrMonthly  {
            cell.viewAlreadyBuy.isHidden = false
            cell.viewBuy.isHidden = true
            
           /*if idOfYearlyOrMonthlySubscription ==  Utility.getStringSafely(strData: modelObj.object(forKey: "Id")) {
                cell.viewAlreadyBuy.isHidden = false
                cell.viewBuy.isHidden = true
            }else{
                if Utility.getStringSafely(strData: modelObj.object(forKey: "purchase_status")) == "true" {
                    cell.viewAlreadyBuy.isHidden = false
                    cell.viewBuy.isHidden = true
                }else{
                    cell.viewAlreadyBuy.isHidden = true
                    cell.viewBuy.isHidden = true
                }
            }*/
        }else{
            if Utility.getStringSafely(strData: modelObj.object(forKey: "purchase_status")) == "true" {
                cell.viewAlreadyBuy.isHidden = false
                cell.viewBuy.isHidden = true
            }else{
                cell.viewAlreadyBuy.isHidden = true
                cell.viewBuy.isHidden = false
            }
        }
        cell.btnSubsDetail.isHidden = true
        cell.subsDetailHeight.constant = 0
        cell.btnSubsDetail.addTarget(self, action:#selector(onClickSubDetail) , for: .touchUpInside)
        if Utility.getStringSafely(strData: modelObj.object(forKey: "IsBestValue")) == String(1) {
            cell.viewBestValue.isHidden = false
            if Utility.getStringSafely(strData: modelObj.object(forKey: "ProductId")) == PickPlugProducts.PickPlugYearlyPackage {
                cell.lblEndDateHeightCons.constant = 20
                cell.lblEndDate.isHidden =  false
                cell.btnSubsDetail.isHidden = false
                cell.subsDetailHeight.constant = 12
                cell.lblEndDate.text = "Unlock picks from all sports for 365 days."
            }else if Utility.getStringSafely(strData: modelObj.object(forKey: "ProductId")) == PickPlugProducts.PickPlugMonthlyRenewPackage {
                cell.lblEndDateHeightCons.constant = 20
                cell.lblEndDate.isHidden =  false
                cell.btnSubsDetail.isHidden = false
                cell.subsDetailHeight.constant = 12
                cell.lblEndDate.text = "Unlock picks from all sports for 30 days."
            }else{
                cell.lblEndDate.isHidden =  true
                cell.lblEndDateHeightCons.constant = 0
                cell.lblEndDate.text = "End Date : "+Utility.getStringSafely(strData: modelObj.object(forKey: "EndDate"))
            }
        }else{
            cell.viewBestValue.isHidden = true
            cell.lblEndDateHeightCons.constant = 20
            cell.lblEndDate.isHidden =  false
            cell.lblEndDate.text = "End Date : "+Utility.getStringSafely(strData: modelObj.object(forKey: "EndDate"))
        }
       
        return cell
    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return UITableViewAutomaticDimension;
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
    }
    
    @objc func onClickSubDetail() {
        let vc = self.storyboard?.instantiateViewController(withIdentifier: "SubscriptionConfirm_VC") as! SubscriptionConfirmVC
        vc.productId =  ""
        self.navigationController?.pushViewController(vc, animated: true)
    }
    
    @objc func onClickBuy(sender: Any) {
        let btnBuy =  sender as! UIButton
        let modelObj = arrData.object(at: btnBuy.tag) as! NSDictionary
        let productID =  Utility.getStringSafely(strData: modelObj.object(forKey: "ProductId"))
        Utility.writeString(key: Utility.KEY_TRANSACTION_IDENTIFIER, value: "")        
        if Utility.getStringSafely(strData: modelObj.object(forKey: "ProductId")) == PickPlugProducts.PickPlugMonthlyRenewPackage || Utility.getStringSafely(strData: modelObj.object(forKey: "ProductId")) == PickPlugProducts.PickPlugYearlyPackage {
            let vc = self.storyboard?.instantiateViewController(withIdentifier: "SubscriptionConfirm_VC") as! SubscriptionConfirmVC
            vc.productId =  productID
            self.navigationController?.pushViewController(vc, animated: true)
        }else{
            for (_, product) in products.enumerated() {
                if product.productIdentifier == productID {
                    PickPlugProducts.store.buyProduct(product)
                    break
                }
            }
        }
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
