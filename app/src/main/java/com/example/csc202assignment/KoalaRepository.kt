import android.content.Context
import androidx.room.Room
import com.example.csc202assignment.Koala
import com.example.csc202assignment.database.KoalaDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "koala-database"
class KoalaRepository private constructor(context: Context, private val coroutineScope: CoroutineScope = GlobalScope) {
    private val executor = Executors.newSingleThreadExecutor()
    private val database: KoalaDatabase = Room
        .databaseBuilder(
            context.applicationContext,
            KoalaDatabase::class.java,
            DATABASE_NAME
        ).build()
    fun getKoalas(): Flow<List<Koala>> = database.koalaDao().getKoalas()

    suspend fun getKoala(id: UUID): Koala = database.koalaDao().getKoala(id)

    fun updateKoala(koala: Koala) {
        coroutineScope.launch {
            database.koalaDao().updateKoala(koala)
        }
    }

    fun deleteKoala(koala: Koala){
        executor.execute {
            database.koalaDao().delete(koala)
        }
    }
     fun addKoala(koala: Koala) {
        executor.execute{
        database.koalaDao().insertAll(koala)
    }}
    companion object {
        private var INSTANCE: KoalaRepository? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = KoalaRepository(context)
            }
        }
        fun get(): KoalaRepository {
            return INSTANCE ?:
            throw IllegalStateException("KoalaRepository must be initialized")
        }

    }
    init {
      // addKoala(Koala())
    }
}
