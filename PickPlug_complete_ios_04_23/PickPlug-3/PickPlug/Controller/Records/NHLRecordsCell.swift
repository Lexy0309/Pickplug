//
//  NHLRecordsCell.swift
//  PickPlug
//
//  Created by abhinav on 27/06/18.
//  Copyright © 2018 abhinav. All rights reserved.
//

import UIKit

class NHLRecordsCell: UITableViewCell {

    @IBOutlet var lblDate: AILabel!
    @IBOutlet var lblMonth: AILabel!
    @IBOutlet var imgTick: UIImageView!
    @IBOutlet var imgPicksFirst: UIImageView!
    @IBOutlet var lblPicksFirst: AILabel!
    @IBOutlet var imgPickSecond: UIImageView!
    @IBOutlet var lblPickSecond: AILabel!
    @IBOutlet var winViewFirst: UIView!
    @IBOutlet var winViewSecond: UIView!
    @IBOutlet var lblLossFirst: AILabel!
    @IBOutlet var lblLossSecond: AILabel!
    @IBOutlet var mainView: UIView!
    
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
}
