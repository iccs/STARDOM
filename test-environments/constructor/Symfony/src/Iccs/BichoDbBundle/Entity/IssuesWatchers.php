<?php

namespace Iccs\BichoDbBundle\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * Iccs\BichoDbBundle\Entity\IssuesWatchers
 */
class IssuesWatchers
{
    /**
     * @var integer $id
     */
    private $id;

    /**
     * @var integer $issueId
     */
    private $issueId;

    /**
     * @var integer $personId
     */
    private $personId;


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
     * Set issueId
     *
     * @param integer $issueId
     */
    public function setIssueId($issueId)
    {
        $this->issueId = $issueId;
    }

    /**
     * Get issueId
     *
     * @return integer 
     */
    public function getIssueId()
    {
        return $this->issueId;
    }

    /**
     * Set personId
     *
     * @param integer $personId
     */
    public function setPersonId($personId)
    {
        $this->personId = $personId;
    }

    /**
     * Get personId
     *
     * @return integer 
     */
    public function getPersonId()
    {
        return $this->personId;
    }
}