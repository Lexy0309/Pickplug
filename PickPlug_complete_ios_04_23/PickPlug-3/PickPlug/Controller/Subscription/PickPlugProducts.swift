/*
* Copyright (c) 2016 Razeware LLC
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
* THE SOFTWARE.
*/

import Foundation

public struct PickPlugProducts {
  
    public static let PickPlugYearlyPackage = "com.pickplug.yearly_renew"
    public static let PickPlugMonthlyRenewPackage = "com.pickplug.monthly_auto_renew_subscription"
    public static let PickPlugParlayFullPackage = "com.pickplug.parlay_non_renew"
    public static let PickPlugNCAAFFullPackage = "com.pickplug.ncaaf_non_renew"
    public static let PickPlugMLBFullPackage = "com.pickplug.mlb_non_renew"
    public static let PickPlugNFLFullPackage = "com.pickplug.nfl_non_renew"
    public static let PickPlugNBAFullPackage = "com.pickplug.nba_non_renew"
    public static let PickPlugNCAABFullPackage = "com.pickplug.ncaab_non_renew"
    public static let PickPlugNHLFullPackage = "com.pickplug.nhl_non_renew"
  
  fileprivate static let productIdentifiers: Set<ProductIdentifier> = [PickPlugYearlyPackage,PickPlugMonthlyRenewPackage,PickPlugParlayFullPackage,PickPlugNCAAFFullPackage,PickPlugMLBFullPackage,PickPlugNFLFullPackage,PickPlugNBAFullPackage,PickPlugNCAABFullPackage,PickPlugNHLFullPackage]

  public static let store = IAPHelper(productIds: PickPlugProducts.productIdentifiers)
}

func resourceNameForProductIdentifier(_ productIdentifier: String) -> String? {
  return productIdentifier.components(separatedBy: ".").last
}
