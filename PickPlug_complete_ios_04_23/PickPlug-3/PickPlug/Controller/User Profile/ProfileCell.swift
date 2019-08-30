//
//  ProfileCell.swift
//  PickPlug
//
//  Created by abhinav on 26/06/18.
//  Copyright © 2018 abhinav. All rights reserved.
//

import UIKit

class ProfileCell: UITableViewCell {

    @IBOutlet var imgTitleImage: UIImageView!
    @IBOutlet var imgHint: UIImageView!
    @IBOutlet var lblTitle: AILabel!
    @IBOutlet var lblDate: AILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
}
