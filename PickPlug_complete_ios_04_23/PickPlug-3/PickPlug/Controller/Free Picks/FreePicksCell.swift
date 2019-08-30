//
//  FreePicksCell.swift
//  PickPlug
//
//  Created by abhinav on 27/06/18.
//  Copyright © 2018 abhinav. All rights reserved.
//

import UIKit

class FreePicksCell: UITableViewCell {

    @IBOutlet var imgFirst: UIImageView!
    @IBOutlet var lblFirst: AILabel!
    @IBOutlet var imgMiddle: UIImageView!
    @IBOutlet var lblSecond: AILabel!
    @IBOutlet var imgSecond: UIImageView!
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
