package s18alg.myapplication

import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import s18alg.myapplication.model.Profile
import s18alg.myapplication.model.TargetWebsite
import java.time.LocalDateTime

class NotificationServiceTest {

    val workingHour = LocalDateTime.of(2018, 8, 28, 15, 0, 0)
    val weekendTime = LocalDateTime.of(2018, 8, 26, 15, 0, 0)
    val offWorkingHour = LocalDateTime.of(2018, 8, 28, 21, 0, 0)

    val targetPe = TargetWebsite("t1", Profile.Personal)
    val targetPr = TargetWebsite("t2", Profile.Professional)
    val targetOt = TargetWebsite("t3", Profile.Other)

    var service = spy(NotificationService::class.java)

    @Before
    fun initTest() {
        targetPr.id = 1
        targetOt.id = 2
    }

    @Test
    fun isTargetTobeNotifiedTest() {
        assert(service.isTargetTobeNotified(targetPe, workingHour))
        assert(service.isTargetTobeNotified(targetPe, weekendTime))
        assert(service.isTargetTobeNotified(targetPe, offWorkingHour))

        assert(service.isTargetTobeNotified(targetPr, workingHour))
        assert(!service.isTargetTobeNotified(targetPr, weekendTime))
        assert(!service.isTargetTobeNotified(targetPr, offWorkingHour))

        assert(!service.isTargetTobeNotified(targetOt, workingHour))
        assert(!service.isTargetTobeNotified(targetOt, weekendTime))
        assert(!service.isTargetTobeNotified(targetOt, offWorkingHour))
    }

    @Test
    fun addTargetTest() {
        service.addTargetToNotification(targetPr)
        assert(service.getDownTargetSize() == 1)

        service.addTargetToNotification(targetPr)
        assert(service.getDownTargetSize() == 1)

        service.addTargetToNotification(targetOt)
        service.addTargetToNotification(targetPe)
        assert(service.getDownTargetSize() == 3)
    }

    @Test
    fun removeTargetTest() {
        service.addTargetToNotification(targetPr)
        service.addTargetToNotification(targetOt)
        service.addTargetToNotification(targetPe)

        service.removeTargetFromNotification(targetPe)
        assert(service.getDownTargetSize() == 2)
        service.removeTargetFromNotification(targetPe)
        assert(service.getDownTargetSize() == 2)

    }
}