package s18alg.myapplication.presenter

import s18alg.myapplication.model.TargetWebsite

interface TargetPresentation {

    fun showTargets(targets: List<TargetWebsite>)
    fun targetAddedAt(position: Int)
    fun scrollTo(position: Int)
}