<?php

namespace Iccs\CvsanalyDbBundle\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * Iccs\CvsanalyDbBundle\Entity\Scmlog
 */
class Scmlog
{
    /**
     * @var integer $id
     */
    private $id;

    /**
     * @var text $rev
     */
    private $rev;

    /**
     * @var integer $committerId
     */
    private $committerId;

    /**
     * @var integer $authorId
     */
    private $authorId;

    /**
     * @var datetime $date
     */
    private $date;

    /**
     * @var text $message
     */
    private $message;

    /**
     * @var boolean $composedRev
     */
    private $composedRev;

    /**
     * @var integer $repositoryId
     */
    private $repositoryId;


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
     * Set rev
     *
     * @param text $rev
     */
    public function setRev($rev)
    {
        $this->rev = $rev;
    }

    /**
     * Get rev
     *
     * @return text 
     */
    public function getRev()
    {
        return $this->rev;
    }

    /**
     * Set committerId
     *
     * @param integer $committerId
     */
    public function setCommitterId($committerId)
    {
        $this->committerId = $committerId;
    }

    /**
     * Get committerId
     *
     * @return integer 
     */
    public function getCommitterId()
    {
        return $this->committerId;
    }

    /**
     * Set authorId
     *
     * @param integer $authorId
     */
    public function setAuthorId($authorId)
    {
        $this->authorId = $authorId;
    }

    /**
     * Get authorId
     *
     * @return integer 
     */
    public function getAuthorId()
    {
        return $this->authorId;
    }

    /**
     * Set date
     *
     * @param datetime $date
     */
    public function setDate($date)
    {
        $this->date = $date;
    }

    /**
     * Get date
     *
     * @return datetime 
     */
    public function getDate()
    {
        return $this->date;
    }

    /**
     * Set message
     *
     * @param text $message
     */
    public function setMessage($message)
    {
        $this->message = $message;
    }

    /**
     * Get message
     *
     * @return text 
     */
    public function getMessage()
    {
        return $this->message;
    }

    /**
     * Set composedRev
     *
     * @param boolean $composedRev
     */
    public function setComposedRev($composedRev)
    {
        $this->composedRev = $composedRev;
    }

    /**
     * Get composedRev
     *
     * @return boolean 
     */
    public function getComposedRev()
    {
        return $this->composedRev;
    }

    /**
     * Set repositoryId
     *
     * @param integer $repositoryId
     */
    public function setRepositoryId($repositoryId)
    {
        $this->repositoryId = $repositoryId;
    }

    /**
     * Get repositoryId
     *
     * @return integer 
     */
    public function getRepositoryId()
    {
        return $this->repositoryId;
    }
}