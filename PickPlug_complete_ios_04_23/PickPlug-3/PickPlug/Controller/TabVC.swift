//
//  TabVC.swift
//  PickPlug
//
//  Created by abhinav on 27/06/18.
//  Copyright © 2018 abhinav. All rights reserved.
//

import UIKit

class TabVC: UITabBarController,UITabBarControllerDelegate {

    var arrTabData = NSMutableArray.init()
    var isPresent = ""
    
    override func viewDidLoad() {
        super.viewDidLoad()
        print("arrTabData",arrTabData,"==== tab ")
        // Do any additional setup after loading the view.
        
        self.delegate = self
        
        //customizableViewControllers = nil
        let vc = self.storyboard?.instantiateViewController(withIdentifier: "ParleyPic_VC") as! ParleyPicVC
        let vc1 = self.storyboard?.instantiateViewController(withIdentifier: "FreePicks_VC") as! FreePicksVC
        let vc2 = self.storyboard?.instantiateViewController(withIdentifier: "NHLRecords_VC") as! NHLRecordsVC
        let vc3 = self.storyboard?.instantiateViewController(withIdentifier: "SportsBook_VC") as! SportsBookVC
      
        vc.title  = "Picks"
        vc1.title = "Picks"
        vc2.title = "Records"
        vc3.title = "Sportsbooks"
        if isPresent == "not_Available"{
            self.viewControllers = [vc,vc2]
        }else{
            self.viewControllers = [vc1,vc2]
        }
       let  arrayOfImageNameForUnselectedState = ["img_picks_white","img_record_white","img_sportbook_white"]
       let  arrayOfImageNameForSelectedState = ["img_pick_yellow","img_record_yellow","img_sportbook_yellow"]
        
        if let count = self.tabBar.items?.count {
            for i in 0...(count-1) {
                let imageNameForSelectedState   = arrayOfImageNameForSelectedState[i]
                let imageNameForUnselectedState = arrayOfImageNameForUnselectedState[i]
                self.tabBar.items?[i].selectedImage = UIImage(named: imageNameForSelectedState )?.withRenderingMode(.alwaysOriginal)
                self.tabBar.items?[i].image = UIImage(named: imageNameForUnselectedState )?.withRenderingMode(.alwaysOriginal)
            }
        }
        UITabBarItem.appearance().setTitleTextAttributes([NSAttributedStringKey.foregroundColor : Utility.hexStringToUIColor(hex: "#ffffff")], for:.normal)
        UITabBarItem.appearance().setTitleTextAttributes([NSAttributedStringKey.foregroundColor : Utility.hexStringToUIColor(hex:"#f7eb42")], for:.selected)
        
    }
    
    
    func tabBarController(_ tabBarController: UITabBarController, shouldSelect viewController: UIViewController) -> Bool {
        
        if Utility.readString(key: Utility.KEY_IS_TYPE_PICKS) == "Free"{
            if viewController == tabBarController.viewControllers![1] {
                return false
            }
        }
        
        return true
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
