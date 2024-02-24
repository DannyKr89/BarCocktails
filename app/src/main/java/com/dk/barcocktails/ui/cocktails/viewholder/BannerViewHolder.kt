package com.dk.barcocktails.ui.cocktails.viewholder

import com.dk.barcocktails.databinding.ItemBannerBinding
import com.yandex.mobile.ads.banner.BannerAdSize
import com.yandex.mobile.ads.common.AdRequest
import kotlin.math.roundToInt

class BannerViewHolder(private val binding: ItemBannerBinding) :
    ItemViewHolder(binding) {

    fun bind() {
        with(binding) {
            val adRequest = AdRequest.Builder().build()
            bannerView.apply {
                setAdUnitId("R-M-6139358-1")
                setAdSize(
                    BannerAdSize.stickySize(
                        rootView.context.applicationContext,
                        (resources.displayMetrics.widthPixels / resources.displayMetrics.density).roundToInt()
                    )
                )
                loadAd(adRequest)
            }
        }
    }
}