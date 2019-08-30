//
//  SubscriptionConfirmVC.swift
//  PickPlug
//
//  Created by Archive Infotech on 30/07/18.
//  Copyright © 2018 abhinav. All rights reserved.
//

import UIKit
import StoreKit

class SubscriptionConfirmVC: UIViewController,HeaderViewBackDelegate {

    @IBOutlet weak var headerView: HeaderView!
    var productId : String = ""
    var products = [SKProduct]()
    @IBOutlet weak var btnTerm: UIButton!
    @IBOutlet weak var btnPolicy: UIButton!
    @IBOutlet weak var scrollViewBottomCons: NSLayoutConstraint!
    @IBOutlet weak var btnContinue: UIButton!
    
    @IBAction func onClikcTermsOfUse(_ sender: Any) {
        let vc = self.storyboard?.instantiateViewController(withIdentifier: "Legal_VC") as! LegalVC
        vc.strHeader = "TERMS OF USE"
        self.navigationController?.pushViewController(vc, animated: true)
    }
    
    @IBAction func onClickPrivacyPolicy(_ sender: Any) {
        let vc = self.storyboard?.instantiateViewController(withIdentifier: "Legal_VC") as! LegalVC
        vc.strHeader = "PRIVACY POLICY"
        self.navigationController?.pushViewController(vc, animated: true)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        headerView.backDelegate = self
        headerView.viewMenu.isHidden = true
        headerView.viewNotify.isHidden = true
        headerView.viewBack.isHidden = false
        headerView.btnRestore.isHidden = true
        headerView.lblHeader.text = "Subscription Details"
        
        let appDelegate  = UIApplication.shared.delegate as! AppDelegate        
        products =  appDelegate.products
        
        let yourAttributes : [NSAttributedStringKey: Any] = [            
            NSAttributedStringKey.foregroundColor : UIColor.blue,
            NSAttributedStringKey.underlineStyle : NSUnderlineStyle.styleSingle.rawValue]
        
        let attribute1String = NSMutableAttributedString(string: "Terms Of Service",attributes: yourAttributes)
        let attribute2String = NSMutableAttributedString(string: "Privacy Policy",attributes: yourAttributes)
        
        btnTerm.setAttributedTitle(attribute1String, for: .normal)
        btnPolicy.setAttributedTitle(attribute2String, for: .normal)
        
        
        if productId == "" {
            scrollViewBottomCons.constant = 0;
            btnContinue.isHidden = true
        }
        
        // Do any additional setup after loading the view.
    }
    
    func onClickBack(sender: Any) {
        self.navigationController?.popViewController(animated: true)
    }
    
    
    @IBAction func onClickContinue(_ sender: Any) {
        for (_, product) in products.enumerated() {
            if product.productIdentifier == productId {
                PickPlugProducts.store.buyProduct(product)
                self.navigationController?.popViewController(animated: true)
                break
            }
        }
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
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
