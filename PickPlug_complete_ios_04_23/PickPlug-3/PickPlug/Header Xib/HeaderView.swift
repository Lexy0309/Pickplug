//
//  HeaderView.swift
//  TCBC
//
//  Created by abhinav on 09/01/18.
//  Copyright © 2018 abhinav. All rights reserved.
//

import UIKit


protocol HeaderViewDelegate {
    func onClickMenu(sender:Any)
    func onClickSearch(sender:Any)
}

protocol HeaderViewBackDelegate {
    func onClickBack(sender:Any)
}
class HeaderView: UIView {
    
    @IBOutlet var btnMenu: AIButton!
    @IBOutlet var lblHeader: AILabel!
    @IBOutlet var btnSearch: AIButton!
    var parentController : UIViewController!
    @IBOutlet var btnShuttle: AIButton!
    @IBOutlet var txtSearch: AITextField!
    @IBOutlet var viewSearch: UIView!
    @IBOutlet var viewTextSrch: UIView!
    @IBOutlet var viewMenu: UIView!
    @IBOutlet var bottomLine: UIView!
    @IBOutlet var imgMenu: UIImageView!
    @IBOutlet weak var viewBack: UIView!
    
    @IBOutlet weak var imgTopLogo: UIImageView!
    @IBOutlet weak var viewTop: UIView!
    @IBOutlet var containerView: UIView!
    var delegate : HeaderViewDelegate!
    var backDelegate : HeaderViewBackDelegate!
    
    
    @IBOutlet var btnReset: AIButton!
    @IBOutlet var lblCount: AILabel!
    @IBOutlet var lblBack: AILabel!
    @IBOutlet var imgSearch: UIImageView!
    @IBOutlet var viewNotify: UIView!
    @IBOutlet var btnNotify: AIButton!
    @IBOutlet var btnRestore: AIButton!
    
    override init(frame: CGRect) {
        super.init(frame: frame)
       
        initView()
    }
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        initView()
    }
    
    func initView(){
        
        
        Bundle.main.loadNibNamed("HeaderXib", owner: self, options: nil)
        addSubview(containerView)
        containerView.frame = self.bounds
        containerView.autoresizingMask = [.flexibleHeight,.flexibleWidth]
        Utility.decorateButton(button: btnRestore, borderColor: "#ffffff", borderWidth: 4, cornerRadius: 10)
    }
    @IBAction func onClickBack(_ sender: Any) {
        backDelegate.onClickBack(sender: sender)
    }
    
    @IBAction func onClickBtnMenu(_ sender: Any) {
        delegate.onClickMenu(sender: sender)
    }
    
    @IBAction func onClickBtnSearch(_ sender: Any) {
        delegate.onClickSearch(sender: sender)
    }
    
}




