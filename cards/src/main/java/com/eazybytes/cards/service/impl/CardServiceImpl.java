package com.eazybytes.cards.service.impl;

import com.eazybytes.cards.constants.CardsConstants;
import com.eazybytes.cards.dto.CardDTO;
import com.eazybytes.cards.entity.Card;
import com.eazybytes.cards.exception.CardAlreadyExistsException;
import com.eazybytes.cards.exception.ResourceNotFoundException;
import com.eazybytes.cards.mapper.CardMapper;
import com.eazybytes.cards.repository.CardRepository;
import com.eazybytes.cards.service.ICardsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements ICardsService {

    private final CardRepository cardRepository;

    /**
     * @param mobileNumber - Mobile Number of the Customer
     */
    @Override
    public void createCard(String mobileNumber) {
        Optional<Card> card = cardRepository.findByMobileNumber(mobileNumber);
        if (card.isPresent()) {
            throw new CardAlreadyExistsException("Card already registered with given mobile number " + mobileNumber);
        }
        Card newCard = createNewCard(mobileNumber);
        try {
            cardRepository.save(newCard);
        } catch (Exception e) {
            throw new RuntimeException("Error creating card for " + mobileNumber);
        }
    }

    /**
     * @param mobileNumber - Input mobile Number
     * @return Card Details based on a given mobileNumber
     */
    @Override
    public CardDTO fetchCard(String mobileNumber) {
        System.out.println("Service fetch request received");
        Card card = cardRepository.findByMobileNumber(mobileNumber).orElseThrow(() -> new ResourceNotFoundException("Card associated with: " + mobileNumber + "does not exist"));
        return CardMapper.mapToCardDTO(card);
    }

    /**
     * @param cardsDTO - CardDTO Object
     * @return boolean indicating if the update of card details is successful or not
     */
    @Override
    public boolean updateCard(CardDTO cardsDTO) {
        // Retrieve card
        Card card = cardRepository.findByMobileNumber(cardsDTO.getMobileNumber()).orElseThrow(() -> new ResourceNotFoundException("Card associated with: " + cardsDTO.getMobileNumber() + "does not exist"));
        Card updatedCard = CardMapper.mapToCard(cardsDTO, card);
        try {
            cardRepository.save(updatedCard);
            return true;
        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    /**
     * @param mobileNumber - Input Mobile Number
     * @return boolean indicating if the delete of card details is successful or not
     */
    @Override
    public boolean deleteCard(String mobileNumber) {
        // Retrieve card
        Card card = cardRepository.findByMobileNumber(mobileNumber).orElseThrow(() -> new ResourceNotFoundException("Card associated with: " + mobileNumber + "not found"));
        try {
            cardRepository.deleteById(card.getCardId());
        } catch (Exception e) {
            throw new RuntimeException("Error deleting card: " + mobileNumber + "\nContact dev support");
        }
        return true;
    }

    /**
     * @param mobileNumber - Mobile Number of the Customer
     * @return the new card details
     */
    private Card createNewCard(String mobileNumber) {
        Card newCard = new Card();
        long randomCardNumber = (long) (new Random().nextDouble() * 1_000_000_0000L) + 1_000_000_0000L;
        newCard.setCardNumber(Long.toString(randomCardNumber));
        newCard.setMobileNumber(mobileNumber);
        newCard.setCardType(CardsConstants.CREDIT_CARD);
        newCard.setTotalLimit(CardsConstants.NEW_CARD_LIMIT);
        newCard.setAmountUsed(0);
        newCard.setAvailableAmount(CardsConstants.NEW_CARD_LIMIT);
        return newCard;
    }
}
