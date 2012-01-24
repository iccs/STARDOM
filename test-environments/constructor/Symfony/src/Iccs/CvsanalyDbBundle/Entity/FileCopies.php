<?php

namespace Iccs\CvsanalyDbBundle\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * Iccs\CvsanalyDbBundle\Entity\FileCopies
 */
class FileCopies
{
    /**
     * @var integer $id
     */
    private $id;

    /**
     * @var integer $toId
     */
    private $toId;

    /**
     * @var integer $fromId
     */
    private $fromId;

    /**
     * @var integer $fromCommitId
     */
    private $fromCommitId;

    /**
     * @var text $newFileName
     */
    private $newFileName;

    /**
     * @var integer $actionId
     */
    private $actionId;


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
     * Set toId
     *
     * @param integer $toId
     */
    public function setToId($toId)
    {
        $this->toId = $toId;
    }

    /**
     * Get toId
     *
     * @return integer 
     */
    public function getToId()
    {
        return $this->toId;
    }

    /**
     * Set fromId
     *
     * @param integer $fromId
     */
    public function setFromId($fromId)
    {
        $this->fromId = $fromId;
    }

    /**
     * Get fromId
     *
     * @return integer 
     */
    public function getFromId()
    {
        return $this->fromId;
    }

    /**
     * Set fromCommitId
     *
     * @param integer $fromCommitId
     */
    public function setFromCommitId($fromCommitId)
    {
        $this->fromCommitId = $fromCommitId;
    }

    /**
     * Get fromCommitId
     *
     * @return integer 
     */
    public function getFromCommitId()
    {
        return $this->fromCommitId;
    }

    /**
     * Set newFileName
     *
     * @param text $newFileName
     */
    public function setNewFileName($newFileName)
    {
        $this->newFileName = $newFileName;
    }

    /**
     * Get newFileName
     *
     * @return text 
     */
    public function getNewFileName()
    {
        return $this->newFileName;
    }

    /**
     * Set actionId
     *
     * @param integer $actionId
     */
    public function setActionId($actionId)
    {
        $this->actionId = $actionId;
    }

    /**
     * Get actionId
     *
     * @return integer 
     */
    public function getActionId()
    {
        return $this->actionId;
    }
}