package s18alg.myapplication.presenter

import s18alg.myapplication.model.TargetDAO
import s18alg.myapplication.model.TargetWebsite
import javax.inject.Inject

class TargetPresenter @Inject constructor(val targetDAO: TargetDAO){
    var targets: ArrayList<TargetWebsite> = arrayListOf()

    var presentation: TargetPresentation? = null

    fun onCreate(targetPresentation: TargetPresentation) {
        presentation = targetPresentation
        loadTarget()
    }

    fun onDestroy() {
        presentation = null
    }

    fun loadTarget() {
        targets.clear()
        targets.addAll(targetDAO.getAll())
        presentation?.showTargets(targets)
    }

    fun addNewTarget(targetUrl: String) {
        val newTarget = TargetWebsite(targetUrl)
        targets.add(newTarget)
        targetDAO.insert(newTarget)
        (targets.size - 1).let {
            presentation?.targetAddedAt(it)
            presentation?.scrollTo(it)
        }
    }
}