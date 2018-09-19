package com.eddystudio.mvvmsample.viewmodel

import io.reactivex.Observable
import io.reactivex.subscribers.TestSubscriber
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import java.lang.RuntimeException

@RunWith(JUnit4::class)
class ActivityVmTest {
  private val activityVM: ActivityVM = Mockito.mock(ActivityVM::class.java)
  private val itemVm = Mockito.mock(ItemVM::class.java)

  @Test
  fun getCompletedEventTest() {

    val vmList = ArrayList<ItemVM>()
    vmList.add(itemVm)
    val completedEvent = ActivityVM.Event.OnCompeleted(vmList)

    Mockito.`when`(activityVM.getEvent()).thenReturn(Observable.just(completedEvent))
    val observable = activityVM.getEvent()
    val testObservable = observable.test()
    testObservable.assertNoErrors()
    testObservable.assertResult(completedEvent)
    testObservable.assertComplete()
  }

  @Test
  fun getErrorEventTest() {
    val errorEvent = ActivityVM.Event.OnError(Throwable("this is expected error"))

    Mockito.`when`(activityVM.getEvent()).thenReturn(Observable.just(errorEvent))
    val observable = activityVM.getEvent()
    val testObservable = observable.test()
    testObservable.assertNoErrors()
    testObservable.assertResult(errorEvent)
    testObservable.assertComplete()
  }

  @Test
  fun getEventOnErrorTest() {
    val error = "this is expected error"
    Mockito.`when`(activityVM.getEvent()).thenReturn(Observable.error(RuntimeException(error)))

    val observable = activityVM.getEvent()
    val testObserver = observable.test()
    testObserver.assertFailureAndMessage(RuntimeException::class.java, error)
    testObserver.assertNotComplete()
  }

  @Test
  fun getDataTest() {
    val testSubscriber = TestSubscriber<List<ActivityVM.Event>>()
    
  }

}