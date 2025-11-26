package ipca.example.habitslistapp.ui.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import ipca.example.habitslistapp.ui.models.Habit

import ipca.example.habitslistapp.ui.repository.ResultWrapper
import ipca.example.habitslistapp.ui.repository.snapshotFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class HabitRepository @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) {

    fun fetchMyHabits(): Flow<ResultWrapper<List<Habit>>> = flow {
        emit(ResultWrapper.Loading())

        try {
            val userId = auth.currentUser?.uid ?: throw Exception("User not logged in")

            db.collection("habits")
                .whereEqualTo("criadoPor", userId)
                .snapshotFlow()
                .collect { snapshot ->

                    val habits = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(Habit::class.java)?.copy(id = doc.id)
                    }

                    emit(ResultWrapper.Success(habits))
                }

        } catch (e: Exception) {
            emit(ResultWrapper.Error(e.localizedMessage ?: "Erro inesperado"))
        }
    }.flowOn(Dispatchers.IO)


    fun fetchSharedHabits(): Flow<ResultWrapper<List<Habit>>> = flow {
        emit(ResultWrapper.Loading())

        try {
            val email = auth.currentUser?.email ?: throw Exception("Email not found")

            db.collection("habits")
                .whereArrayContains("partilhadoCom", email)
                .snapshotFlow()
                .collect { snapshot ->

                    val shared = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(Habit::class.java)?.copy(id = doc.id)
                    }

                    emit(ResultWrapper.Success(shared))
                }

        } catch (e: Exception) {
            emit(ResultWrapper.Error(e.localizedMessage ?: "Erro inesperado"))
        }
    }.flowOn(Dispatchers.IO)


    fun addHabit(habit: Habit): Flow<ResultWrapper<Unit>> = flow {
        emit(ResultWrapper.Loading())

        try {
            db.collection("habits")
                .add(habit)
                .await()

            emit(ResultWrapper.Success(Unit))

        } catch (e: Exception) {
            emit(ResultWrapper.Error(e.localizedMessage ?: "Erro ao adicionar"))
        }
    }.flowOn(Dispatchers.IO)


    fun updateHabit(id: String, nome: String, descricao: String): Flow<ResultWrapper<Unit>> = flow {
        emit(ResultWrapper.Loading())

        try {
            db.collection("habits")
                .document(id)
                .update(mapOf("nome" to nome, "descricao" to descricao))
                .await()

            emit(ResultWrapper.Success(Unit))

        } catch (e: Exception) {
            emit(ResultWrapper.Error("Erro ao atualizar"))
        }
    }.flowOn(Dispatchers.IO)


    fun deleteHabit(id: String): Flow<ResultWrapper<Unit>> = flow {
        emit(ResultWrapper.Loading())

        try {
            db.collection("habits")
                .document(id)
                .delete()
                .await()

            emit(ResultWrapper.Success(Unit))

        } catch (e: Exception) {
            emit(ResultWrapper.Error("Erro ao eliminar"))
        }
    }.flowOn(Dispatchers.IO)


    fun shareHabit(id: String, email: String): Flow<ResultWrapper<Unit>> = flow {
        emit(ResultWrapper.Loading())

        try {
            db.collection("habits")
                .document(id)
                .update(
                    "partilhadoCom",
                    com.google.firebase.firestore.FieldValue.arrayUnion(email)
                )
                .await()

            emit(ResultWrapper.Success(Unit))

        } catch (e: Exception) {
            emit(ResultWrapper.Error("Erro ao partilhar hábito"))
        }
    }.flowOn(Dispatchers.IO)
}
