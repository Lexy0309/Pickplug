//
//  ParleyPicVC.swift
//  PickPlug
//
//  Created by abhinav on 28/06/18.
//  Copyright © 2018 abhinav. All rights reserved.
//

import UIKit

class ParleyPicVC: UIViewController,HeaderViewBackDelegate {

    @IBOutlet var headerView: HeaderView!
    @IBOutlet var lblDetail: AILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        headerView.backDelegate = self
        headerView.viewMenu.isHidden = true
        headerView.viewBack.isHidden = false
        headerView.lblHeader.text = Utility.readString(key: Utility.KEY_IS_TYPE_PICKS) + " Picks"
    }
    
    func onClickBack(sender: Any) {
        self.navigationController?.popViewController(animated: true)
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
