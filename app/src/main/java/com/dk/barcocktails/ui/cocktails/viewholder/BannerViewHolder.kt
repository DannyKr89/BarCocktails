package com.dk.barcocktails.ui.cocktails.viewholder

import com.dk.barcocktails.databinding.ItemBannerBinding
import com.yandex.mobile.ads.banner.BannerAdEventListener
import com.yandex.mobile.ads.banner.BannerAdSize
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
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
                setBannerAdEventListener(object : BannerAdEventListener {
                    override fun onAdLoaded() {
                        if (this@BannerViewHolder.binding.root.context == null) {
                            bannerView.destroy()
                        }
                    }

                    override fun onAdFailedToLoad(p0: AdRequestError) = Unit

                    override fun onAdClicked() = Unit

                    override fun onLeftApplication() = Unit

                    override fun onReturnedToApplication() = Unit

                    override fun onImpression(p0: ImpressionData?) = Unit
                })
                loadAd(adRequest)
            }
        }
    }
}