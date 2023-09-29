package eu.vendeli.tgbot.interfaces.features

import eu.vendeli.tgbot.interfaces.IActionState
import eu.vendeli.tgbot.types.MessageEntity
import eu.vendeli.tgbot.utils.builders.EntitiesBuilder

/**
 * Entities feature, see [Features article](https://github.com/vendelieu/telegram-bot/wiki/Features)
 *
 * @param Return Action class itself.
 */
interface EntitiesFeature<Return> : IActionState, Feature {
    @Suppress("UNCHECKED_CAST")
    private val thisAsReturn: Return
        get() = this as Return

    /**
     * Entities adding DSL
     */
    fun entities(block: EntitiesBuilder.() -> Unit): Return = entities(EntitiesBuilder().apply(block).listOfEntities)

    /**
     * Add Entities directly
     */
    fun entities(entities: List<MessageEntity>): Return {
        parameters["entities"] = entities
        return thisAsReturn
    }
}
