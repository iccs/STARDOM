<?php

namespace Iccs\BichoDbBundle\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 *
 *
 * Iccs\BichoDbBundle\Entity\People
 */
class People
{
    /**
     * @var integer $id
     */
    private $id;

    /**
     * @var string $name
     */
    private $name;

    /**
     * @var string $email
     */
    private $email;

    /**
     * @var string $userId
     */
    private $userId;

    /**
     * @var integer $trackerId
     */
    private $trackerId;


    /**
     * Get id
     *
     * @return integer 
     */
    public function getId()
    {
        return $this->id;
    }

    /**
     * Set name
     *
     * @param string $name
     */
    public function setName($name)
    {
        $this->name = $name;
    }

    /**
     * Get name
     *
     * @return string 
     */
    public function getName()
    {
        return $this->name;
    }

    /**
     * Set email
     *
     * @param string $email
     */
    public function setEmail($email)
    {
        $this->email = $email;
    }

    /**
     * Get email
     *
     * @return string 
     */
    public function getEmail()
    {
        return $this->email;
    }

    /**
     * Set userId
     *
     * @param string $userId
     */
    public function setUserId($userId)
    {
        $this->userId = $userId;
    }

    /**
     * Get userId
     *
     * @return string 
     */
    public function getUserId()
    {
        return $this->userId;
    }

    /**
     * Set trackerId
     *
     * @param integer $trackerId
     */
    public function setTrackerId($trackerId)
    {
        $this->trackerId = $trackerId;
    }

    /**
     * Get trackerId
     *
     * @return integer 
     */
    public function getTrackerId()
    {
        return $this->trackerId;
    }
}