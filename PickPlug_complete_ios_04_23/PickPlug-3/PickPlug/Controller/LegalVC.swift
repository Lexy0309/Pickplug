//
//  LegalVC.swift
//  PickPlug
//
//  Created by abhinav on 27/06/18.
//  Copyright © 2018 abhinav. All rights reserved.
//

import UIKit
import WebKit

class LegalVC: UIViewController,HeaderViewBackDelegate,WKUIDelegate,WKNavigationDelegate,UIScrollViewDelegate {

    @IBOutlet var headerView: HeaderView!
    @IBOutlet var lblLegalTitle: AILabel!
    @IBOutlet var lblLegalDetail: AILabel!
    @IBOutlet var webContainerView: UIView!
    @IBOutlet var progressBar: UIProgressView!
    @IBOutlet var webParentView: UIView!
    var mediaWebViewObj: WKWebView!
    var strHeader: String = ""
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        headerView.backDelegate = self
        headerView.viewMenu.isHidden = true
        headerView.viewBack.isHidden = false
        headerView.lblHeader.text = strHeader
        
        let config = WKWebViewConfiguration()
        config.allowsInlineMediaPlayback = true;
        mediaWebViewObj = WKWebView(frame: webContainerView.frame, configuration: config)
        mediaWebViewObj.uiDelegate = self
        mediaWebViewObj.scrollView.delegate = self
        mediaWebViewObj.addObserver(self, forKeyPath: #keyPath(WKWebView.estimatedProgress), options: .new, context: nil)
        webContainerView.addSubview(mediaWebViewObj)
        
        if (Utility .isInternetAvailable()){
            self.loadRequest()
        }else{
            Utility.showSnackBar(message: Utility.KEY_NO_INTERNET)
        }
    }
    
    override func observeValue(forKeyPath keyPath: String?, of object: Any?, change: [NSKeyValueChangeKey : Any]?, context: UnsafeMutableRawPointer?) {
        if keyPath == "estimatedProgress" {
            progressBar.progress = Float(mediaWebViewObj.estimatedProgress)
        }
    }
    func loadRequest() {
        progressBar.isHidden = false
        let url = URL (string: strHeader == "TERMS OF USE" ? Utility.LEGAL_URL : Utility.PRIVACY_URL)
        let   requestObj = URLRequest(url: url!)
        print("requestObj",requestObj)
        mediaWebViewObj.load(requestObj)
        mediaWebViewObj.scrollView.bounces = false
        mediaWebViewObj.navigationDelegate = self
    }
    
    func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!) {
        progressBar.setProgress(1.0, animated: true);
        progressBar.isHidden = true
    }
    override func viewDidAppear(_ animated: Bool) {
        mediaWebViewObj.frame = webContainerView.bounds
    }
    func onClickBack(sender: Any) {
        self.navigationController?.popViewController(animated: true)
    }
    override var preferredStatusBarStyle: UIStatusBarStyle{
        return .lightContent
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
