//
//  SplashVC.swift
//  PickPlug
//
//  Created by abhinav on 06/07/18.
//  Copyright © 2018 abhinav. All rights reserved.
//

import UIKit

class SplashVC: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.1) {
            if (!Utility.readString(key: Utility.KEY_EMAIL).isEmpty && !Utility.readString(key: Utility.KEY_PASSWORD).isEmpty) || Utility.readString(key: Utility.KEY_IS_LOGIN_FACEBOOK) == "yes" {
                let storyboard: UIStoryboard = self.storyboard! //UIStoryboard(name: "Home", bundle: nil)
                let controller = storyboard.instantiateViewController(withIdentifier: "KYDrawerController") as! KYDrawerController
                let navController = UINavigationController.init(rootViewController: controller)
                navController.setNavigationBarHidden(true, animated: false)
                self.present(navController, animated: true, completion: nil)
            }else {
                let vc  = self.storyboard?.instantiateViewController(withIdentifier: "Login_VC") as! LoginVC
                self.navigationController?.pushViewController(vc, animated: true)
            }
        }
        //let vc  = self.storyboard?.instantiateViewController(withIdentifier: "Login_VC") as! LoginVC
        //self.navigationController?.pushViewController(vc, animated: true)
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
