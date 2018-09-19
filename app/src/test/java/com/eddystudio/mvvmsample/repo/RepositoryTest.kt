package com.eddystudio.mvvmsample.repo

import com.eddystudio.mvvmsample.model.ProductItem
import com.eddystudio.mvvmsample.service.ProductService
import io.reactivex.Observable
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import java.lang.RuntimeException

@RunWith(JUnit4::class)
class RepositoryTest {
  private val repository: Repository = Mockito.mock(Repository::class.java)

  @Test
  fun getProductOnSuccess() {
    Mockito.`when`(repository.getProduct("")).thenReturn(Observable.just(emptyList<ProductItem>()))
    val observable = repository.getProduct("")
    val testObservable = observable!!.test()
    testObservable.assertNoErrors()
    testObservable.events
    testObservable.assertComplete()

    Mockito.verify(repository).getProduct("")
  }


  @Test
  fun getProductOnError() {
    val errorMessage = "Expected exception"
    Mockito.`when`(repository.getProduct("")).thenReturn(Observable.just(emptyList<ProductItem>()).doOnNext{
     throw RuntimeException(errorMessage)
    })
    val observable = repository.getProduct("")
    val testObservable = observable!!.test()
    testObservable.assertFailureAndMessage(RuntimeException::class.java, errorMessage)
    testObservable.assertNotComplete()
  }
}
