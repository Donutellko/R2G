package ga.patrick.r2g.bpp

import mu.KLogging
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.stereotype.Component
import java.lang.reflect.Proxy

@Component
class MeasureTimeBeanPostProcessor : BeanPostProcessor {

    private val beanData: MutableMap<String, Class<out Any>> = mutableMapOf()
    private val measurementsData: MutableMap<String, MutableList<Long>> = mutableMapOf()

    override fun postProcessBeforeInitialization(bean: Any, beanName: String): Any {
        val type = bean::class.java
        if (type.isAnnotationPresent(MeasureTime::class.java)) {
            beanData[beanName] = type
        }
        return bean
    }

    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any {
        val type = beanData[beanName] ?: return bean

        return Proxy.newProxyInstance(type.classLoader, type.interfaces)
        { proxy, method, args ->
            val beginTime = System.nanoTime()
            val result = method.invoke(bean, *args)
            val totalTime = System.nanoTime() - beginTime

            measurementsData.compute(type.name) { name, list ->
                (list ?: mutableListOf())
                        .apply { add(totalTime) }
            }

//            counter++
//            if (counter % 10 == 0) {
            measurementsData.forEach { (t, u) ->
                println(t + ": " + u.joinToString("\t"))
            }
//            }

//            logger.info { "Invocation of ${type.name}.${method.name} took: $totalTime ms. " }
            result
        }
    }

    companion object : KLogging()
}