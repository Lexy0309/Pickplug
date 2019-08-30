//
//  PickDetailVC.swift
//  PickPlug
//
//  Created by abhinav on 28/06/18.
//  Copyright © 2018 abhinav. All rights reserved.
//

import UIKit
import SDWebImage
import StoreKit

class PickDetailVC: UIViewController,HeaderViewBackDelegate,UITableViewDelegate,UITableViewDataSource,ServiceApiResponseDelegate,DismissAlertDelegate {

    
    @IBOutlet var headerView: HeaderView!
    @IBOutlet var viewDate: UIView!
    @IBOutlet var lblDate: AILabel!
    @IBOutlet var imgFirst: UIImageView!
    @IBOutlet var lblFirst: AILabel!
    @IBOutlet var imgMiddle: UIImageView!
    @IBOutlet var imgSecond: UIImageView!
    @IBOutlet var lblSecond: AILabel!
    @IBOutlet var NHLView: UIView!
    @IBOutlet var lblNHLOptionBelow: AILabel!
    @IBOutlet var dataTableView: UITableView!
    @IBOutlet var freePickView: UIView!
    @IBOutlet var lblFreePick: AILabel!
    @IBOutlet var freePickDetailView: UIView!
    @IBOutlet var lblFreePickDetail: AILabel!
    var arrData = NSMutableArray.init()
    var modelObj = NSDictionary.init()
    var strPresent = ""
    var strMiddleImage = ""
    @IBOutlet var detailViewTopCons: NSLayoutConstraint!
    @IBOutlet var imgBackTrailingCons: NSLayoutConstraint!
    @IBOutlet var imgBackLeadingCons: NSLayoutConstraint!
    @IBOutlet var teamViewHeightCons: NSLayoutConstraint!
    @IBOutlet var imgBackImage: UIImageView!
    @IBOutlet var viewTeam: UIView!
    var strImgUrlPrefix = ""
    var strIsAllow : String = ""
    var allSubscription = NSMutableArray.init()
    var products = [SKProduct]()
    var arrPurchasedProducts = NSMutableArray.init()
    var backToRootView = false
    @IBOutlet weak var btnTerm: UIButton!
    @IBOutlet weak var btnPolicy: UIButton!
    @IBOutlet var infoView: UIView!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        headerView.backDelegate = self
        headerView.viewMenu.isHidden = true
        headerView.viewBack.isHidden = false
        headerView.lblHeader.text = Utility.readString(key: Utility.KEY_IS_TYPE_PICKS) + strPresent
        dataTableView.delegate = self
        dataTableView.dataSource = self
        dataTableView.estimatedRowHeight = 85
        dataTableView.rowHeight  = UITableViewAutomaticDimension
        dataTableView.tableFooterView = infoView
        freePickDetailView.dropShadow()
        viewDate.dropShadow()
        print("modelObj",modelObj)
        
        if strIsAllow == "yes" || Utility.readString(key: Utility.KEY_IS_TYPE_PICKS) == "Free" {
            freePickView.isHidden = false
            NHLView.isHidden = true
        }else{
            freePickView.isHidden = true
            NHLView.isHidden = false
            
            let appDelegate  = UIApplication.shared.delegate as! AppDelegate
            if appDelegate.products.count > 0 {
                products =  appDelegate.products
                getSubscribeDetails();
            }else{
                Utility.showSnackBar(message: "Something went wrong. Please try after some time")
            }
        }
        
        
        imgMiddle.sd_setImage(with: URL(string:Utility.getStringSafely(strData: modelObj.object(forKey: "sportImage"))), completed: nil)
        
        if strPresent == " Picks"{
            if Utility.readString(key: Utility.KEY_PICKS_SPORT_ID) == "1" {
                lblDate.text = Utility.formattedDateFromString(dateString: Utility.getStringSafely(strData: modelObj.object(forKey: "WeekDate")), withFormat: "EEEE MMM d - hh:mm a", enterFormat: "dd-MM-yyyy HH:mm:ss")
            }else{
                lblDate.text = Utility.formattedDateFromString(dateString: Utility.getStringSafely(strData: modelObj.object(forKey: "PickDate")), withFormat: "EEEE MMM d - hh:mm a", enterFormat: "dd-MM-yyyy HH:mm:ss")
            }
            
        }else{
            lblDate.text = Utility.formattedDateFromString(dateString: Utility.getStringSafely(strData: modelObj.object(forKey: "PickDate")), withFormat: "EEEE MMM d - hh:mm a", enterFormat: "dd-MM-yyyy HH:mm:ss")
        }
        
        if (modelObj.object(forKey: "HomeTeamDetails") as! NSDictionary).count > 0{
            lblSecond.text = Utility.getStringSafely(strData: (modelObj.object(forKey: "HomeTeamDetails") as! NSDictionary).object(forKey: "TeamName"))
            imgSecond.sd_setImage(with: URL(string:Utility.getStringSafely(strData: (modelObj.object(forKey: "HomeTeamDetails") as! NSDictionary).object(forKey: "TeamIcon"))), completed: nil)
        }
        if (modelObj.object(forKey: "VisitingTeamDetails") as! NSDictionary).count > 0{
            lblFirst.text = Utility.getStringSafely(strData: (modelObj.object(forKey: "VisitingTeamDetails") as! NSDictionary).object(forKey: "TeamName"))
            imgFirst.sd_setImage(with: URL(string:Utility.getStringSafely(strData: (modelObj.object(forKey: "VisitingTeamDetails") as! NSDictionary).object(forKey: "TeamIcon"))), completed: nil)
        }
        lblFreePickDetail.text = Utility.getStringSafely(strData: (modelObj.object(forKey: "PickAnalysis"))).removeHTMLTag()
        lblFreePick.text = Utility.getStringSafely(strData: (modelObj.object(forKey: "PickRecord")))
        
        
        
        NotificationCenter.default.addObserver(self, selector: #selector(SubscriptionVC.handlePurchaseNotification(_:)),
                                               name: NSNotification.Name(rawValue: IAPHelper.IAPHelperPurchaseNotification),
                                               object: nil)
        
        let yourAttributes : [NSAttributedStringKey: Any] = [
            NSAttributedStringKey.foregroundColor : UIColor.blue,
            NSAttributedStringKey.underlineStyle : NSUnderlineStyle.styleSingle.rawValue]
        
        let attribute1String = NSMutableAttributedString(string: "Terms Of Service",attributes: yourAttributes)
        let attribute2String = NSMutableAttributedString(string: "Privacy Policy",attributes: yourAttributes)
        
        btnTerm.setAttributedTitle(attribute1String, for: .normal)
        btnPolicy.setAttributedTitle(attribute2String, for: .normal)
        
        
    }
    
    @objc func handlePurchaseNotification(_ notification: Notification) {
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
        var controller : PickDetailVC!
        func serviceResponseCallBack(response: Any) {
            Utility.hideLoader()
            print("Response call back",response)
            let resultObj = (response as! NSDictionary).object(forKey: "Results") as! NSDictionary
            if resultObj.object(forKey: "User_Subscriptions") != nil {
                if (resultObj.object(forKey: "User_Subscriptions") as! NSArray).count > 0 {
                    print("call back")
                    Utility.showAlert(title: "Thank you for your purchase!", message: "If you have any questions or concerns, please email: picks@pickplug.com", controller: controller, delegate: controller)
                    
                    controller.freePickView.isHidden = false
                    controller.NHLView.isHidden = true
                    controller.backToRootView = true
                    //controller.tabBarController?.navigationController?.popToRootViewController(animated: true)
                    //controller.navigationController?.popToRootViewController(animated: true)
                    //NotificationCenter.default.post(name: NSNotification.Name(rawValue: "BackToHome"), object: nil)
                }
            }
            
        }
    }

    
    func onDismissAlert() {
        
    }
    
    @IBAction func onClickTermsOfUse(_ sender: Any) {
        let vc = self.storyboard?.instantiateViewController(withIdentifier: "Legal_VC") as! LegalVC
        vc.strHeader = "TERMS OF USE"
        self.navigationController?.pushViewController(vc, animated: true)
    }
    
    @IBAction func onClickPrivacyPolicy(_ sender: Any) {
        let vc = self.storyboard?.instantiateViewController(withIdentifier: "Legal_VC") as! LegalVC
        vc.strHeader = "PRIVACY POLICY"
        self.navigationController?.pushViewController(vc, animated: true)
    }
    
    func getSubscribeDetails(){
        if Utility.isInternetAvailable() {
            Utility.showLoader()
            Utility.sendRequest(forUrl: Utility.GET_SUBSCRIPTION , parameters: ["user":Utility.readString(key: Utility.KEY_USER_ID)], requestMethod: AIHTTPMethod.post, others: [:], withDelegate: self)
        }else{
            Utility.showSnackBar(message: Utility.KEY_NO_INTERNET)
        }
    }
    
    func serviceResponseCallBack(response: Any) {
        
        print("Response ",response)
        let resultObj = (response as! NSDictionary).object(forKey: "Results") as! NSDictionary
        if (resultObj.object(forKey: "Subscriptions") as! NSArray).count > 0 {
            allSubscription = (resultObj.object(forKey: "Subscriptions") as! NSArray).mutableCopy() as! NSMutableArray
            updateData()
        }
        Utility.hideLoader()
    }
    
    
    func updateData() {
        arrData = NSMutableArray.init()
        
        for i in 0..<allSubscription.count{
            let modelObj = NSMutableDictionary.init(dictionary: allSubscription.object(at: i) as! NSDictionary)
            if Utility.readString(key: Utility.KEY_PICKS_SPORT_ID) == Utility.getStringSafely(strData: modelObj.object(forKey: "Sport")) || Utility.getStringSafely(strData: modelObj.object(forKey: "Sport")) == "0" {
                modelObj.setObject("false", forKey: "purchase_status" as NSCopying)
                print("1");
                let productID =  Utility.getStringSafely(strData: modelObj.object(forKey: "ProductId"))
                for (_, product) in products.enumerated() {
                    print( "2 ",product.productIdentifier,"===",productID);
                    if product.productIdentifier == productID {
                        print("3");
                        if IAPHelper.canMakePayments() {
                            print("4");
                            priceFormatter.locale = product.priceLocale
                            modelObj.setObject(priceFormatter.string(from: product.price)!, forKey: "price" as NSCopying)
                            arrData.add(modelObj)
                        }
                    }
                }
            }
            
        }
        self.dataTableView.reloadData()
    }
    
    
    override func viewDidAppear(_ animated: Bool) {
        if UIDevice.current.userInterfaceIdiom == .pad{
            imgBackTrailingCons.constant = -50
            imgBackLeadingCons.constant = -50
            //detailViewTopCons.constant = 40
            viewTeam.dropShadowSubscription()
            imgBackImage.clipsToBounds = true
            //teamViewHeightCons.constant = 100
        }else{
            imgBackImage.clipsToBounds = false
            //teamViewHeightCons.constant = 70
            //detailViewTopCons.constant = 25
            imgBackTrailingCons.constant = -25
            imgBackLeadingCons.constant = -25
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    func onClickBack(sender: Any) {
        if backToRootView {
            self.navigationController?.popToRootViewController(animated: true)
        }else{
            self.navigationController?.popViewController(animated: true)
        }
        
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
        
        cell.viewAlreadyBuy.isHidden = true
        cell.viewBuy.isHidden = false

        cell.btnSubsDetail.isHidden = true
        cell.subsDetailHeight.constant = 0
        //cell.btnSubsDetail.addTarget(self, action:#selector(onClickSubDetail) , for: .touchUpInside)
        
        if Utility.getStringSafely(strData: modelObj.object(forKey: "IsBestValue")) == String(1) {
            cell.viewBestValue.isHidden = false
            if Utility.getStringSafely(strData: modelObj.object(forKey: "ProductId")) == PickPlugProducts.PickPlugYearlyPackage {
                cell.lblEndDateHeightCons.constant = 20
                cell.lblEndDate.isHidden =  false
                cell.lblEndDate.text = "Unlock picks from all sports for 365 days."
            }else if Utility.getStringSafely(strData: modelObj.object(forKey: "ProductId")) == PickPlugProducts.PickPlugMonthlyRenewPackage {
                cell.lblEndDateHeightCons.constant = 20
                cell.lblEndDate.isHidden =  false
                cell.lblEndDate.text = "Unlock picks from all sports for 30 days."
            }else{
                cell.lblEndDate.isHidden =  true
                cell.lblEndDateHeightCons.constant = 0
                cell.lblEndDate.text = "End Date : "+Utility.getStringSafely(strData: modelObj.object(forKey: "EndDate"))
            }
        }else{
            cell.lblEndDateHeightCons.constant = 20
            cell.lblEndDate.isHidden =  false
            cell.viewBestValue.isHidden = true
            cell.lblEndDate.text = "End Date : "+Utility.getStringSafely(strData: modelObj.object(forKey: "EndDate"))
        }
        
        return cell
    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return UITableViewAutomaticDimension;
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
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
extension String {
    func removeHTMLTag() -> String {
        return self.replacingOccurrences(of: "<[^>]+>", with: "", options: String.CompareOptions.regularExpression, range: nil)
    }
}
